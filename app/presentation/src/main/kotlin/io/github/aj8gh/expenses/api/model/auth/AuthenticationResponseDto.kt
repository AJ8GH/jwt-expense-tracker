package io.github.aj8gh.expenses.api.model.auth

import io.github.aj8gh.expenses.model.auth.AuthenticationResponse

data class AuthenticationResponseDto(
  val accessToken: String,
  val refreshToken: String,
)

fun toDto(response: AuthenticationResponse) = AuthenticationResponseDto(
  accessToken = response.accessToken,
  refreshToken = response.refreshToken,
)
