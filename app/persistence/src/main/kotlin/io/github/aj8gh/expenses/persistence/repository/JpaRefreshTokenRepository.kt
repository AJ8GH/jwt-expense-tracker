package io.github.aj8gh.expenses.persistence.repository

import io.github.aj8gh.expenses.persistence.model.PartyEntity
import io.github.aj8gh.expenses.persistence.model.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.NativeQuery
import java.util.*

interface JpaRefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {

  @NativeQuery(
    """
    SELECT
      p.id,
      p.username,
      p.password,
      p.role,
      p.status,
      p.created_at,
      p.updated_at
    FROM refresh_token rt
    INNER JOIN party p
    ON rt.party_id = p.id
    WHERE rt.token = :token
  """
  )
  fun findUserDetailsByToken(token: String): PartyEntity?
}
