package io.github.aj8gh.expenses.persistence.model

import io.github.aj8gh.expenses.persistence.model.Role.USER
import io.github.aj8gh.expenses.persistence.model.Status.ACTIVE
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
data class PersonEntity(
  @Id @UuidGenerator val id: UUID,
  val username: String,
  val password: String,
  val role: Role = USER,
  val status: Status = ACTIVE,
  @CreationTimestamp val createdAt: Instant,
  @UpdateTimestamp val updatedAt: Instant,
)

enum class Status {
  ACTIVE
}

enum class Role {
  USER,
  ADMIN
}
