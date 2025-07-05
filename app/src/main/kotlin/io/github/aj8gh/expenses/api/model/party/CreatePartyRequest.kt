package io.github.aj8gh.expenses.api.model.party

import io.github.aj8gh.expenses.persistence.model.Role
import io.github.aj8gh.expenses.persistence.model.Role.USER

data class CreatePartyRequest(
  val username: String,
  val password: String,
  val role: Role = USER,
)
