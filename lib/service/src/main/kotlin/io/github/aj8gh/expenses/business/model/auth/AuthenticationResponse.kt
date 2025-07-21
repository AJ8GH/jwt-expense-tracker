package io.github.aj8gh.expenses.business.model.auth

data class AuthenticationResponse(
  val accessToken: String,
  val refreshToken: String,
)
