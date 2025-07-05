package io.github.aj8gh.expenses.service.user

import io.github.aj8gh.expenses.persistence.repository.PersonRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
  private val personRepository: PersonRepository,
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    return personRepository.findByUsername(username)!!
      .let {
        User.builder()
          .username(it.username)
          .password(it.password)
          .roles(it.role.name)
          .build()
      }
  }
}
