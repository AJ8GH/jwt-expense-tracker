package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.PartyEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PartyRepository(
  private val repository: JpaPartyRepository,
  private val encoder: PasswordEncoder,
) {

  fun findByUsername(username: String): PartyEntity? =
    repository.findByUsername(username)

  fun save(entity: PartyEntity): PartyEntity =
    repository.save(
      PartyEntity(
        username = entity.username,
        password = encoder.encode(entity.password),
        role = entity.role,
      )
    )
}
