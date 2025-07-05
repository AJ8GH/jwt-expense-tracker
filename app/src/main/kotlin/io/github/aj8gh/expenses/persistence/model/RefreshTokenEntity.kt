package io.github.aj8gh.expenses.persistence.model

import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

data class RefreshTokenEntity(
  @Id @UuidGenerator val id: UUID,
  val token: String,
  val userId: UUID,
  @CreationTimestamp val createdAt: Instant? = null,
  @UpdateTimestamp val updatedAt: Instant? = null,
)
