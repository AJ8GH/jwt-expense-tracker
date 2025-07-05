package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.PersonEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PersonRepository(
  private val repository: JpaPersonRepository,
  private val encoder: PasswordEncoder,
) {

  fun findByUsername(username: String): PersonEntity? =
    repository.findByUsername(username)

  fun save(entity: PersonEntity) =
    repository.save(
      PersonEntity(
        username = entity.username,
        password = encoder.encode(entity.password),
        role = entity.role,
      )
    )
}
