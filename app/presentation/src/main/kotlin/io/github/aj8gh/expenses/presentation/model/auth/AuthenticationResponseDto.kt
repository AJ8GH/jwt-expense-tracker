package io.github.aj8gh.expenses.presentation.model.auth

import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse

data class AuthenticationResponseDto(
  val accessToken: String,
  val refreshToken: String,
)

fun toDto(response: AuthenticationResponse) = AuthenticationResponseDto(
  accessToken = response.accessToken,
  refreshToken = response.refreshToken,
)
