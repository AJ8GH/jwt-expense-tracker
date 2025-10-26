package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.ALL
import io.github.aj8gh.expenses.business.constant.BY_ID
import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.github.aj8gh.expenses.persistence.model.ExpenseEntity
import io.github.aj8gh.expenses.persistence.repository.JpaExpenseRepository
import io.github.aj8gh.expenses.presentation.model.error.ErrorResponse
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpensesRequest
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse
import io.github.aj8gh.expenses.presentation.model.expense.ExpensesResponse
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

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

  @When("the following create expenses request {string} is sent with token {tokenAlias}")
  fun createExpenses(
    aliasString: String,
    token: String,
    expenses: List<CreateExpenseRequest>,
  ) = client.post(
    path = "$EXPENSES_PATH$ALL",
    content = CreateExpensesRequest(expenses),
    responseType = ExpensesResponse::class,
    token = token
  ).body!!.let {
    aliasString.split(",").forEachIndexed { i, alias ->
      aliases.put(alias.trim(), it.expenses[i].id)
    }
  }

  @When("a get all expenses request is sent with token {tokenAlias}")
  fun getAllExpenses(
    token: String,
  ) = client.get(
    path = EXPENSES_PATH,
    responseType = ExpensesResponse::class,
    token = token
  )

  @When("a get expense {alias} request is sent with token {tokenAlias}")
  fun getExpense(
    expense: UUID,
    token: String,
  ) = client.get(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = ExpenseResponse::class,
    token = token
  )

  @When("a delete expense {alias} request is sent with token {tokenAlias}")
  fun deleteExpense(
    expense: UUID,
    token: String,
  ) = client.delete(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = Void::class,
    token = token,
  )

  @When("an erroneous delete expense {alias} request is sent with token {tokenAlias}")
  fun deleteExpenseForError(
    expense: UUID,
    token: String,
  ) = client.delete(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = ErrorResponse::class,
    token = token,
  )

  @When("an erroneous get expense {alias} request is sent with token {tokenAlias}")
  fun getExpenseForError(
    expense: UUID,
    token: String,
  ) = client.get(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = ErrorResponse::class,
    token = token,
  )

  @When("an erroneous put expense {alias} request is sent with token {tokenAlias}")
  fun putExpenseForError(
    expense: UUID,
    token: String,
    request: CreateExpenseRequest,
  ) = client.put(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = ErrorResponse::class,
    content = request,
    token = token,
  )

  @When("a put expense {alias} request is sent with token {tokenAlias}")
  fun putExpense(
    expense: UUID,
    token: String,
    request: CreateExpenseRequest,
  ) = client.put(
    path = "$EXPENSES_PATH$BY_ID",
    pathVariables = arrayOf(expense),
    responseType = ExpenseResponse::class,
    content = request,
    token = token,
  )

  @Then("the following expense is stored")
  fun expenseStored(expected: ExpenseEntity) =
    repository.findById(expected.id!!).orElseThrow() shouldBeEqualUsingFields {
      excludedProperties = setOf(
        ExpenseEntity::updatedAt,
        ExpenseEntity::createdAt,
      )
      expected
    }

  @Then("the following expense is returned")
  fun expenseReturned(expected: ExpenseResponse) =
    scenarioContext.body<ExpenseResponse>() shouldBeEqual expected

  @Then("the following expenses are returned")
  fun expensesReturned(expected: List<ExpenseResponse>) =
    scenarioContext.body<ExpensesResponse>() shouldBeEqual ExpensesResponse(expected)

  @Then("an empty expenses response is returned")
  fun noExpensesReturned() =
    scenarioContext.body<ExpensesResponse>() shouldBeEqual ExpensesResponse()
}
