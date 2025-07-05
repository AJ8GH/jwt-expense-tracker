package io.github.aj8gh.expenses.api.model.auth

data class AuthenticationRequest(
  val username: String,
  val password: String,
)
