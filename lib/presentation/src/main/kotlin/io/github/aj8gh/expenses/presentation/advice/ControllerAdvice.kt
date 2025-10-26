package io.github.aj8gh.expenses.presentation.advice

import io.github.aj8gh.expenses.business.exception.ResourceAuthorizationException
import io.github.aj8gh.expenses.business.exception.ResourceExistsException
import io.github.aj8gh.expenses.business.exception.ResourceNotFoundException
import io.github.aj8gh.expenses.presentation.model.error.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Clock
import java.util.*

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ControllerAdvice(
  private val clock: Clock,
) {

  @ResponseStatus(CONFLICT)
  @ExceptionHandler(
    value = [ResourceExistsException::class],
    produces = [APPLICATION_JSON_VALUE]
  )
  fun handleResourceExists(e: ResourceExistsException) = handle(e)

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(
    value = [ResourceNotFoundException::class],
    produces = [APPLICATION_JSON_VALUE]
  )
  fun handleResourceNotFound(e: ResourceNotFoundException) = handle(e)

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(
    value = [ResourceAuthorizationException::class],
    produces = [APPLICATION_JSON_VALUE]
  )
  fun handleResourceAuthorization(e: ResourceAuthorizationException) = handle(e)

  private fun handle(e: Exception) = ErrorResponse(
    id = UUID.randomUUID(),
    timestamp = clock.instant(),
    message = e.message ?: "No message available"
  ).also {
    logger.error {
      "Handling exception=${e::class}, id=${it.id}, message='${e.message}'"
    }
  }
}
