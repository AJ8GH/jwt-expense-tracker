package io.github.aj8gh.expenses.api.controller

import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.service.security.AuthenticationService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

const val AUTH_PATH = "/auth"
const val REFRESH_PATH = "/refresh"

@RestController
@ResponseStatus(CREATED)
@RequestMapping(AUTH_PATH)
class AuthController(
  private val authService: AuthenticationService,
) {

  @PostMapping
  fun authenticate(
    @RequestBody request: AuthenticationRequest,
  ) = authService.authenticate(request)

  @PostMapping(REFRESH_PATH)
  fun refresh(
    @RequestBody request: RefreshTokenRequest,
  ) = RefreshTokenResponse(authService.refreshAccessToken(request.refreshToken))
}
