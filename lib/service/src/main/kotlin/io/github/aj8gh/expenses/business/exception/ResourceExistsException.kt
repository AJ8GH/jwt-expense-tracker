package io.github.aj8gh.expenses.business.exception

class ResourceExistsException(
  override val message: String,
  override val cause: Throwable? = null,
) : RuntimeException(message, cause)
