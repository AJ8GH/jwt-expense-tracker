package io.github.aj8gh.expenses.service.security

import io.github.aj8gh.expenses.service.security.TokenType.ACCESS
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val BEARER = "Bearer "
private const val ERROR_MESSAGE = """{ "error": "Filter Authorization error: '%s'" }"""
private const val UNKNOWN_ERROR = "unknown error"

@Component
class JwtAuthorizationFilter(
  private val userDetailsService: UserDetailsService,
  private val jwtService: JwtService,
) : OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    authorizeRequest(request, response)
    filterChain.doFilter(request, response)
  }

  private fun authorizeRequest(
    request: HttpServletRequest,
    response: HttpServletResponse,
  ) = request.getHeader(AUTHORIZATION)
    ?.takeIf { it.startsWith(BEARER) }
    ?.let {
      try {
        validateToken(it, request)
      } catch (ex: Exception) {
        response.writer.write(ERROR_MESSAGE.format(ex.message ?: UNKNOWN_ERROR))
      }
    }

  private fun validateToken(authHeader: String, request: HttpServletRequest) =
    SecurityContextHolder.getContext().authentication ?: run {
      val claims = jwtService.extractClaims(authHeader.substringAfter(BEARER))
      userDetailsService.loadUserByUsername(claims.subject)
        .takeIf { claims.subject == it.username && claims[TOKEN_TYPE_CLAIM] == ACCESS.name }
        ?.let { user ->
          UsernamePasswordAuthenticationToken(user, null, user.authorities)
            .also { it.details = WebAuthenticationDetailsSource().buildDetails(request) }
            .also { SecurityContextHolder.getContext().authentication = it }
        }
    }
}
