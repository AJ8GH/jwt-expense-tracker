package io.github.aj8gh.expenses.presentation.model.expense

data class ExpensesResponse(
  val expenses: List<ExpenseResponse> = listOf(),
)
