package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaPersonRepository : JpaRepository<PersonEntity, UUID> {

  fun findByUsername(username: String): PersonEntity?
}
