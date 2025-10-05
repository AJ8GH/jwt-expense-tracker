package io.github.aj8gh.expenses.presentation.model.expense

import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest.Companion.fromRequest
import java.util.*

data class CreateExpensesRequest(
  val expenses: List<CreateExpenseRequest> = listOf(),
) {

  companion object {
    @JvmStatic
    fun fromRequests(from: CreateExpensesRequest, partyId: UUID) =
      from.expenses.map { fromRequest(it, partyId) }
  }
}
