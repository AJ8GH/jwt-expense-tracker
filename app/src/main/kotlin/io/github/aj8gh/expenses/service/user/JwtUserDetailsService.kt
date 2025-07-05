package io.github.aj8gh.expenses.service.user

import io.github.aj8gh.expenses.persistence.repository.PartyRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
  private val partyRepository: PartyRepository,
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    return partyRepository.findByUsername(username)!!
      .let {
        User.builder()
          .username(it.username)
          .password(it.password)
          .roles(it.role.name)
          .build()
      }
  }
}
