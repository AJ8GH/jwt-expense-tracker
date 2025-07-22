package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.business.service.expense.ExpenseService
import io.github.aj8gh.expenses.business.service.party.PartyIdExtractor
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponses
import io.github.aj8gh.expenses.presentation.model.expense.fromRequest
import io.github.aj8gh.expenses.presentation.model.expense.toResponse
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
  fun findAll() = ExpenseResponses()

  @PostMapping
  @ResponseStatus(CREATED)
  fun create(
    @RequestHeader(AUTHORIZATION) bearerToken: String,
    @RequestBody request: CreateExpenseRequest,
  ): ExpenseResponse {
    val partyId = extractor.extract(bearerToken)
    return toResponse(service.create(fromRequest(request, partyId)))
  }
}
