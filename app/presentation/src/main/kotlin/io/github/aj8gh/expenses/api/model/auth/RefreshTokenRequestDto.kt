package io.github.aj8gh.expenses.api.model.auth

import io.github.aj8gh.expenses.model.auth.RefreshTokenRequest

data class RefreshTokenRequestDto(
  val refreshToken: String,
)

fun fromDto(dto: RefreshTokenRequestDto) =
  RefreshTokenRequest(dto.refreshToken)
