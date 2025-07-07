package io.github.aj8gh.expenses.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity(name = "party")
data class PartyEntity(
  @Id @UuidGenerator val id: UUID? = null,
  val username: String,
  val password: String,
  val role: Role = Role.USER,
  val status: Status = Status.ACTIVE,
  @CreationTimestamp val createdAt: Instant? = null,
  @UpdateTimestamp val updatedAt: Instant? = null,
) {

  enum class Role {
    USER,
    ADMIN
  }

  enum class Status {
    ACTIVE
  }
}
