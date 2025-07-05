package io.github.aj8gh.expenses.service.security

import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.api.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.persistence.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
  private val authManager: AuthenticationManager,
  private val userDetailsService: UserDetailsService,
  private val jwtService: JwtService,
  private val refreshTokenRepository: RefreshTokenRepository,
  @Value("\${auth.token.ttl-millis.jwt}") private val accessTokenTtlMillis: Long = 0,
  @Value("\${auth.token.ttl-millis.refresh}") private val refreshTokenTtlMillis: Long = 0,
) {

  fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
    authManager.authenticate(
      UsernamePasswordAuthenticationToken(
        request.username,
        request.password
      )
    )

    val user = userDetailsService.loadUserByUsername(request.username)
    val accessToken = createToken(user.username, accessTokenTtlMillis)
    val refreshToken = createToken(user.username, refreshTokenTtlMillis)
    refreshTokenRepository.save(refreshToken, user)

    return AuthenticationResponse(accessToken = accessToken, refreshToken = refreshToken)
  }

  fun refreshAccessToken(refreshToken: String): String {
    val username = jwtService.extractUsername(refreshToken)
    val userDetails = userDetailsService.loadUserByUsername(username)
    val refreshTokenUserDetails = refreshTokenRepository.findUserByToken(refreshToken)
    if (userDetails.username == refreshTokenUserDetails?.username) {
      return createToken(userDetails.username, accessTokenTtlMillis)
    } else {
      throw AuthenticationServiceException("Invalid refresh token")
    }
  }

  private fun createToken(username: String, ttlMillis: Long) =
    jwtService.generateToken(username, ttlMillis)
}
