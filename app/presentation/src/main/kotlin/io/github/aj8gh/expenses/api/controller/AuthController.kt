package io.github.aj8gh.expenses.api.controller

import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequestDto
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenRequestDto
import io.github.aj8gh.expenses.api.model.auth.fromDto
import io.github.aj8gh.expenses.api.model.auth.toDto
import io.github.aj8gh.expenses.constant.AUTH_PATH
import io.github.aj8gh.expenses.constant.REFRESH_PATH
import io.github.aj8gh.expenses.service.security.AuthenticationService
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
  fun authenticate(
    @RequestBody request: AuthenticationRequestDto,
  ) = toDto(authService.authenticate(fromDto(request)))

  @PostMapping(REFRESH_PATH)
  fun refresh(
    @RequestBody request: RefreshTokenRequestDto,
  ) = toDto(authService.refreshAccessToken(fromDto(request)))
}
