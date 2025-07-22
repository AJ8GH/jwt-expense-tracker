package io.github.aj8gh.expenses.business.service.party

import io.github.aj8gh.expenses.business.repository.PartyRepository
import io.github.aj8gh.expenses.business.service.security.JwtService
import org.springframework.stereotype.Component
import java.util.*

@Component
class PartyIdExtractor(
  private val jwtService: JwtService,
  private val repository: PartyRepository,
) {

  fun extract(bearerToken: String): UUID {
    val username = jwtService.extractClaims(bearerToken.split(" ")[1]).subject
    return repository.findByUsername(username)!!.id!!
  }
}
