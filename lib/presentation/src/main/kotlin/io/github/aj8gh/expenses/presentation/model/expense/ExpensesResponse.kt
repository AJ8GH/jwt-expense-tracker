package io.github.aj8gh.expenses.presentation.model.expense

import io.github.aj8gh.expenses.business.model.expense.Expense
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse.Companion.toResponse

data class ExpensesResponse(
  val expenses: List<ExpenseResponse> = listOf(),
) {

  companion object {
    @JvmStatic
    fun toResponses(from: List<Expense>) = from
      .map { toResponse(it) }
      .let { ExpensesResponse(it) }
  }
}
