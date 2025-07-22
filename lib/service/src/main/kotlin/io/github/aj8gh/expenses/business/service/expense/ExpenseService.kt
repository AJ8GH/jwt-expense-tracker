package io.github.aj8gh.expenses.business.service.expense

import io.github.aj8gh.expenses.business.model.expense.Expense
import io.github.aj8gh.expenses.business.repository.ExpenseRepository
import org.springframework.stereotype.Service

@Service
class ExpenseService(
  private val repository: ExpenseRepository,
) {

  fun create(toCreate: Expense) = repository.save(toCreate)
}
