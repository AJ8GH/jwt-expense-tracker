package io.github.aj8gh.expenses.business.service.party

import io.github.aj8gh.expenses.business.exception.ResourceExistsException
import io.github.aj8gh.expenses.business.model.party.Party
import io.github.aj8gh.expenses.business.repository.PartyRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PartyService(
  private val repository: PartyRepository,
  private val encoder: PasswordEncoder,
) {

  fun create(party: Party): Party {
    validate(party)
    return repository.save(party.encoded(encoder))
  }

  private fun validate(party: Party) {
    repository.findByUsername(party.username)?.let {
      throw ResourceExistsException(
        message = "Party with username ${party.username} already exists"
      )
    }
  }
}
