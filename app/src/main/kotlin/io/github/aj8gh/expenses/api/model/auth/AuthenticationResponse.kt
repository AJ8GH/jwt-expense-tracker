package io.github.aj8gh.expenses.api.model.auth

data class AuthenticationResponse(
  val accessToken: String,
  val refreshToken: String,
)
