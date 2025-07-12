package io.github.aj8gh.expenses.business.service.security

import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.business.repository.RefreshTokenRepository
import io.github.aj8gh.expenses.business.service.security.TokenType.ACCESS
import io.github.aj8gh.expenses.business.service.security.TokenType.REFRESH
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

const val TOKEN_TYPE_CLAIM = "tokenType"

enum class TokenType {
  ACCESS,
  REFRESH,
}

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
    val accessToken = createToken(user.username, accessTokenTtlMillis, ACCESS)
    val refreshToken = createToken(user.username, refreshTokenTtlMillis, REFRESH)
    refreshTokenRepository.save(refreshToken, user)
    return AuthenticationResponse(accessToken = accessToken, refreshToken = refreshToken)
  }

  fun refreshAccessToken(request: RefreshTokenRequest): RefreshTokenResponse {
    try {
      val username = jwtService.extractClaims(request.refreshToken).subject
      val userDetails = userDetailsService.loadUserByUsername(username)
      val refreshTokenUserDetails = refreshTokenRepository.findUserByToken(request.refreshToken)
      if (userDetails.username == refreshTokenUserDetails?.username) {
        return RefreshTokenResponse(createToken(userDetails.username, accessTokenTtlMillis, ACCESS))
      } else {
        throw AuthenticationServiceException("Invalid refresh token")
      }
    } catch (e: Exception) {
      throw AuthenticationServiceException(
        "Exception when refreshing token: ${e.message}",
        e.cause
      )
    }
  }

  private fun createToken(
    username: String,
    ttlMillis: Long,
    tokenType: TokenType,
  ) = jwtService.generateToken(
    username,
    ttlMillis,
    mapOf(Pair(TOKEN_TYPE_CLAIM, tokenType))
  )
}
