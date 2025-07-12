package io.github.aj8gh.expenses.presentation.model.auth

import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest

data class AuthenticationRequestDto(
  val username: String,
  val password: String,
)

fun fromDto(dto: AuthenticationRequestDto) = AuthenticationRequest(
  username = dto.username,
  password = dto.password,
)
