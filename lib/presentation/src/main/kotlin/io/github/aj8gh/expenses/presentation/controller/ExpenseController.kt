package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.ALL
import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.business.service.expense.ExpenseService
import io.github.aj8gh.expenses.business.service.party.PartyIdExtractor
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest.Companion.fromRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpensesRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpensesRequest.Companion.fromRequests
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse.Companion.toResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpensesResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpensesResponse.Companion.toResponses
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EXPENSES_PATH)
class ExpenseController(
  private val service: ExpenseService,
  private val extractor: PartyIdExtractor,
) {

  @GetMapping
  fun findAll(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
  ) = service.getAll(extractor.extract(bearerToken))
    .map { toResponse(it) }
    .let { ExpensesResponse(it) }

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
}
