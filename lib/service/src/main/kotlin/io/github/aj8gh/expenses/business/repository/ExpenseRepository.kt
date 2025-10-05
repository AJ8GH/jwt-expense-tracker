package io.github.aj8gh.expenses.business.repository

import io.github.aj8gh.expenses.business.model.expense.Expense
import io.github.aj8gh.expenses.business.model.expense.fromEntity
import io.github.aj8gh.expenses.business.model.expense.toEntity
import io.github.aj8gh.expenses.persistence.repository.JpaExpenseRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ExpenseRepository(
  private val repository: JpaExpenseRepository,
) {

  fun save(toSave: Expense) = fromEntity(repository.save(toEntity(toSave)))

  fun findAllByPartyId(party: UUID) = repository.findAllByPartyId(party)
    .map { fromEntity(it) }
}
