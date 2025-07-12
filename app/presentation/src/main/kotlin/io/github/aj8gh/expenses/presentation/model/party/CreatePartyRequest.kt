package io.github.aj8gh.expenses.presentation.model.party

import io.github.aj8gh.expenses.business.model.party.Party

data class CreatePartyRequest(
  val username: String,
  val password: String,
  val role: Role = Role.USER,
)

fun fromRequest(request: CreatePartyRequest) = Party(
  username = request.username,
  password = request.password,
  role = Party.Role.valueOf(request.role.name),
)
