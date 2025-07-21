package io.github.aj8gh.expenses.business.repository

import io.github.aj8gh.expenses.business.model.party.Party
import io.github.aj8gh.expenses.business.model.party.fromEntity
import io.github.aj8gh.expenses.business.model.party.toEntity
import io.github.aj8gh.expenses.persistence.model.PartyEntity
import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PartyRepository(
  private val repository: JpaPartyRepository,
  private val encoder: PasswordEncoder,
) {

  fun findByUsername(username: String): PartyEntity? =
    repository.findByUsername(username)

  fun save(party: Party): Party =
    fromEntity(repository.save(toEntity(party)))
}
