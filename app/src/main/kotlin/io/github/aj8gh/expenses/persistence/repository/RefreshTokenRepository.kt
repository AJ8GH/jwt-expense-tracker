package io.github.aj8gh.expenses.persistence.repository

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

// interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
//
//   @NativeQuery(
//     """
//     SELECT
//       p.id
//       p.user_name,
//       p.password,
//       p.role,
//       p.status,
//       p.created_at,
//       p.updated_at
//     FROM refresh_token rt
//     WHERE token = :token
//     INNER JOIN person p
//     ON rt.user_id = p.id
//   """
//   )
//   fun findUserDetailsByToken(token: String): PersonEntity?
//
// }

@Repository
class RefreshTokenRepository {

  private val userDetailsByToken = mutableMapOf<String, UserDetails>()

  fun findUserDetailsByToken(token: String) = userDetailsByToken[token]

  fun save(token: String, userDetails: UserDetails) {
    userDetailsByToken[token] = userDetails
  }
}
