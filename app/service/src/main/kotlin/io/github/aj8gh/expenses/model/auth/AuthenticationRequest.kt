package io.github.aj8gh.expenses.model.auth

data class AuthenticationRequest(
  val username: String,
  val password: String,
)
