package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaExpenseRepository : JpaRepository<ExpenseEntity, UUID> {

  fun findAllByPartyId(partyId: UUID): List<ExpenseEntity>
}
