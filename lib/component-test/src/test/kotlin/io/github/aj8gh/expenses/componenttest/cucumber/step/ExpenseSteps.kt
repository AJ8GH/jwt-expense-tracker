package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.github.aj8gh.expenses.persistence.model.ExpenseEntity
import io.github.aj8gh.expenses.persistence.repository.JpaExpenseRepository
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.equals.shouldBeEqual

class ExpenseSteps(
  private val client: Client,
  private val aliases: Aliases,
  private val scenarioContext: ScenarioContext,
  private val repository: JpaExpenseRepository,
) {

  @When("the following create expense request {string} is sent with token {tokenAlias}")
  fun createExpense(
    alias: String,
    token: String,
    request: CreateExpenseRequest,
  ) = client.post(
    path = EXPENSES_PATH,
    content = request,
    responseType = ExpenseResponse::class,
    token = token
  ).body!!.let { aliases.put(alias, it.id) }

  @Then("the following expense is stored")
  fun expenseReturned(expected: ExpenseEntity) {
    val actual = repository.findById(expected.id!!).orElseThrow()
    actual shouldBeEqualUsingFields {
      excludedProperties = setOf(
        ExpenseEntity::updatedAt,
        ExpenseEntity::createdAt,
      )
      expected
    }
  }

  @Then("the following expense is returned")
  fun expenseReturned(expected: ExpenseResponse) {
    val actual = scenarioContext.body<ExpenseResponse>()
    actual shouldBeEqual expected
  }
}
