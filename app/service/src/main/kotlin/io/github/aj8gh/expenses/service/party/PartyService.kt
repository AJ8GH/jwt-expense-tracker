package io.github.aj8gh.expenses.service.party

import io.github.aj8gh.expenses.model.party.Party
import io.github.aj8gh.expenses.persistence.repository.PartyRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PartyService(
  private val repository: PartyRepository,
  private val encoder: PasswordEncoder,
) {

  fun create(party: Party) = repository.save(party.encoded(encoder))
}
