package io.github.aj8gh.expenses.api.model

import io.github.aj8gh.expenses.api.model.Role.USER
import io.github.aj8gh.expenses.api.model.Status.ACTIVE

data class RegistrationRequest(
  val username: String,
  val password: String,
  val role: Role = USER,
  val status: Status = ACTIVE,
)

enum class Status {
  ACTIVE
}

enum class Role {
  USER,
  ADMIN
}
