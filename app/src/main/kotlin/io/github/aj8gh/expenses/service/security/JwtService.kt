package io.github.aj8gh.expenses.service.security

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*
import java.util.Base64.getDecoder
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "HmacSHA256"

@Service
class JwtService(
  private val clock: Clock,
  @Value("\${auth.jwt.secret}") private val secret: String,
) {

  fun signingKey() = SecretKeySpec(getDecoder().decode(secret), ALGORITHM)

  fun generateToken(
    subject: String,
    ttlMillis: Long,
    additionalClaims: Map<String, Any> = emptyMap(),
  ): String = clock.instant().toEpochMilli().let {
    Jwts.builder()
      .claims(additionalClaims)
      .subject(subject)
      .issuedAt(Date(it))
      .expiration(Date(it + ttlMillis))
      .signWith(signingKey())
      .compact()
  }

  fun extractUsername(token: String): String = Jwts.parser()
    .verifyWith(signingKey())
    .build()
    .parseSignedClaims(token)
    .payload
    .subject
}
