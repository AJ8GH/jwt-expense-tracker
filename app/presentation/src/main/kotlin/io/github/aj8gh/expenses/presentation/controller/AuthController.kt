package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.REFRESH_PATH
import io.github.aj8gh.expenses.business.service.security.AuthenticationService
import io.github.aj8gh.expenses.presentation.model.auth.AuthenticationRequestDto
import io.github.aj8gh.expenses.presentation.model.auth.RefreshTokenRequestDto
import io.github.aj8gh.expenses.presentation.model.auth.fromDto
import io.github.aj8gh.expenses.presentation.model.auth.toDto
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseStatus(CREATED)
@RequestMapping(AUTH_PATH)
class AuthController(
  private val authService: AuthenticationService,
) {

  @PostMapping
  fun authenticate(@RequestBody request: AuthenticationRequestDto) =
    toDto(authService.authenticate(fromDto(request)))

  @PostMapping(REFRESH_PATH)
  fun refresh(@RequestBody request: RefreshTokenRequestDto) =
    toDto(authService.refreshAccessToken(fromDto(request)))
}
