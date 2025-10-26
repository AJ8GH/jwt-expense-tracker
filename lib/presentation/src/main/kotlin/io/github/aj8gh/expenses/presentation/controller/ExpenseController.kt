package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.ALL
import io.github.aj8gh.expenses.business.constant.BY_ID
import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.business.exception.ResourceNotFoundException
import io.github.aj8gh.expenses.business.service.expense.ExpenseService
import io.github.aj8gh.expenses.business.service.party.PartyIdExtractor
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest.Companion.fromRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpensesRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpensesRequest.Companion.fromRequests
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse.Companion.toResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpensesResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpensesResponse.Companion.toResponses
import io.github.aj8gh.expenses.presentation.validation.AuthValidator
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(EXPENSES_PATH)
class ExpenseController(
  private val service: ExpenseService,
  private val extractor: PartyIdExtractor,
  private val authValidator: AuthValidator,
) {

  @GetMapping
  @ResponseStatus(OK)
  fun findAll(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
  ) = service.findAll(extractor.extract(bearerToken))
    .map { toResponse(it) }
    .let { ExpensesResponse(it) }

  @GetMapping(BY_ID)
  @ResponseStatus(OK)
  fun findById(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @PathVariable id: UUID,
  ): ExpenseResponse {
    authValidator.validateExpense(bearerToken, id)
    return service.findById(id)
      .map { toResponse(it) }
      .orElseThrow { ResourceNotFoundException("Expense $id not found") }
  }

  @DeleteMapping(BY_ID)
  @ResponseStatus(NO_CONTENT)
  fun delete(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @PathVariable id: UUID,
  ) {
    authValidator.validateExpense(bearerToken, id)
    service.deleteById(id)
  }

  @PostMapping
  @ResponseStatus(CREATED)
  fun create(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @RequestBody request: CreateExpenseRequest,
  ) = fromRequest(request, extractor.extract(bearerToken))
    .let { service.create(it) }
    .let { toResponse(it) }

  @PostMapping(ALL)
  @ResponseStatus(CREATED)
  fun createAll(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @RequestBody request: CreateExpensesRequest,
  ) = fromRequests(request, extractor.extract(bearerToken))
    .let { service.createAll(it) }
    .let { toResponses(it) }

  @PutMapping(BY_ID)
  @ResponseStatus(CREATED)
  fun update(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @PathVariable id: UUID,
    @RequestBody request: CreateExpenseRequest,
  ): ExpenseResponse {
    authValidator.validateExpense(bearerToken, id)
    return fromRequest(request, extractor.extract(bearerToken), id)
      .let { service.update(it) }
      .map { toResponse(it) }
      .orElseThrow { ResourceNotFoundException("Expense $id not found") }
  }
}
