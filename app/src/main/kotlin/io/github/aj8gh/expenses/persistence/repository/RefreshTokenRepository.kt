package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.RefreshTokenEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepository(
  private val tokenRepository: JpaRefreshTokenRepository,
  private val personRepository: PersonRepository,
) {
  fun findUserByToken(token: String) = tokenRepository.findUserDetailsByToken(token)

  fun save(token: String, userDetails: UserDetails) {
    tokenRepository.save(
      RefreshTokenEntity(
        token = token,
        personId = personRepository.findByUsername(userDetails.username)!!.id!!,
      )
    )
  }
}
