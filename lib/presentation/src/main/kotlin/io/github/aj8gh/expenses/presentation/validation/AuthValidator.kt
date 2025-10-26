package io.github.aj8gh.expenses.presentation.validation

import io.github.aj8gh.expenses.business.exception.ResourceAuthorizationException
import io.github.aj8gh.expenses.business.service.expense.ExpenseService
import io.github.aj8gh.expenses.business.service.party.PartyIdExtractor
import io.github.aj8gh.expenses.presentation.validation.ResourceType.EXPENSE
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthValidator(
  private val partyIdExtractor: PartyIdExtractor,
  private val expenseService: ExpenseService,
) {

  fun validateExpense(authHeader: String, resourceId: UUID) {
    val partyId = partyIdExtractor.extract(authHeader)
    expenseService.findById(resourceId)
      .ifPresent {
        if (partyId != it.partyId) {
          throw ResourceAuthorizationException.from(
            partyId = partyId,
            resourceId = resourceId,
            type = EXPENSE.name
          )
        }
      }
  }
}
