package io.github.aj8gh.expenses.business.model.auth

data class AuthenticationRequest(
  val username: String,
  val password: String,
)
