package io.github.aj8gh.expenses.api.model.auth

import io.github.aj8gh.expenses.model.auth.RefreshTokenResponse

class RefreshTokenResponseDto(
  val accessToken: String,
)

fun toDto(response: RefreshTokenResponse) =
  RefreshTokenResponseDto(response.accessToken)
