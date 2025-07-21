package io.github.aj8gh.expenses.presentation.model.auth

import io.github.aj8gh.expenses.business.model.auth.RefreshTokenRequest

data class RefreshTokenRequestDto(
  val refreshToken: String,
)

fun fromDto(dto: RefreshTokenRequestDto) =
  RefreshTokenRequest(dto.refreshToken)
