package io.github.aj8gh.expenses.api.model.party

import io.github.aj8gh.expenses.model.party.Party
import io.github.aj8gh.expenses.model.party.Party.Status
import java.util.*

data class PartyResponse(
  val id: UUID,
  val username: String,
  val password: String,
  val role: Role = Role.USER,
  val status: Status = Status.ACTIVE,
)

fun toResponse(party: Party) = PartyResponse(
  id = party.id!!,
  username = party.username,
  password = party.password,
  role = Role.valueOf(party.role.name),
  status = Status.valueOf(party.status.name),
)
