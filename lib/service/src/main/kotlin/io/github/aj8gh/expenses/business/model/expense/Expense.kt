package io.github.aj8gh.expenses.business.model.expense

import io.github.aj8gh.expenses.persistence.model.ExpenseEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class Expense(
  val id: UUID? = null,
  val partyId: UUID,
  val category: Category,
  val amount: BigDecimal,
  val date: LocalDate,
  val description: String? = null,
) {

  enum class Category {
    FOOD,
    BILLS,
    MORTGAGE,
    RENT,
    ENTERTAINMENT,
    TRAVEL,
    CLOTHING,
    HOME_MAINTENANCE,
    OTHER,
  }
}

fun toEntity(model: Expense, entity: ExpenseEntity? = null) = ExpenseEntity(
  id = model.id,
  category = ExpenseEntity.Category.valueOf(model.category.name),
  partyId = model.partyId,
  amount = model.amount,
  date = model.date,
  description = model.description,
  createdAt = entity?.createdAt
)

fun fromEntity(entity: ExpenseEntity) = Expense(
  id = entity.id,
  category = Expense.Category.valueOf(entity.category.name),
  partyId = entity.partyId,
  amount = entity.amount,
  date = entity.date,
  description = entity.description,
)
