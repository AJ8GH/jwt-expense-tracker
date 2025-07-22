package io.github.aj8gh.expenses.business.repository

import io.github.aj8gh.expenses.persistence.model.RefreshTokenEntity
import io.github.aj8gh.expenses.persistence.repository.JpaRefreshTokenRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepository(
  private val tokenRepository: JpaRefreshTokenRepository,
  private val partyRepository: PartyRepository,
) {

  fun findUserByToken(token: String) = tokenRepository.findUserDetailsByToken(token)

  fun save(token: String, userDetails: UserDetails) {
    tokenRepository.save(
      RefreshTokenEntity(
        token = token,
        partyId = partyRepository.findByUsername(userDetails.username)!!.id!!,
      )
    )
  }
}
