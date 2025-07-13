package io.github.aj8gh.expenses.presentation.model.error

import java.time.Instant
import java.util.*

data class ErrorResponse(
  val id: UUID,
  val timestamp: Instant,
  val message: String,
)
