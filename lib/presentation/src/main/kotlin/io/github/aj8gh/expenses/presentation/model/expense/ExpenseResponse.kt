package io.github.aj8gh.expenses.presentation.model.expense

import io.github.aj8gh.expenses.business.model.expense.Expense
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class ExpenseResponse(
  val id: UUID,
  val partyId: UUID,
  val category: Category,
  val amount: BigDecimal,
  val date: LocalDate,
  val description: String? = null,
)

fun toResponse(created: Expense) = ExpenseResponse(
  id = created.id!!,
  category = Category.valueOf(created.category.name),
  partyId = created.partyId,
  amount = created.amount,
  date = created.date,
  description = created.description,
)
