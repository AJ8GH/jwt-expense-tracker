package io.github.aj8gh.expenses.presentation.model.auth

import io.github.aj8gh.expenses.business.model.auth.RefreshTokenResponse

class RefreshTokenResponseDto(
  val accessToken: String,
)

fun toDto(response: RefreshTokenResponse) =
  RefreshTokenResponseDto(response.accessToken)
