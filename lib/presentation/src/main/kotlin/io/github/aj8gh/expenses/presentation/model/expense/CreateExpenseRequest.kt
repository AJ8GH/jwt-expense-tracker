package io.github.aj8gh.expenses.presentation.model.expense

import io.github.aj8gh.expenses.business.model.expense.Expense
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class CreateExpenseRequest(
  val category: Category,
  val amount: BigDecimal,
  val date: LocalDate,
  val description: String? = null,
)

fun fromRequest(request: CreateExpenseRequest, partyId: UUID) = Expense(
  category = Expense.Category.valueOf(request.category.name),
  partyId = partyId,
  amount = request.amount,
  date = request.date,
  description = request.description,
)
