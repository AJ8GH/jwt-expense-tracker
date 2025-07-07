package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.PartyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaPartyRepository : JpaRepository<PartyEntity, UUID> {

  fun findByUsername(username: String): PartyEntity?
}
