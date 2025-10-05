package io.github.aj8gh.expenses.business.service.expense

import io.github.aj8gh.expenses.business.model.expense.Expense
import io.github.aj8gh.expenses.business.repository.ExpenseRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpenseService(
  private val repository: ExpenseRepository,
) {

  fun create(toCreate: Expense) = repository.save(toCreate)

  fun getAll(party: UUID) = repository.findAllByPartyId(party)
}
