package io.github.aj8gh.expenses.business.model.party

import io.github.aj8gh.expenses.persistence.model.PartyEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

data class Party(
  val id: UUID? = null,
  val username: String,
  val password: String,
  val role: Role = Role.USER,
  val status: Status = Status.ACTIVE,
) {

  enum class Role {
    USER,
    ADMIN
  }

  enum class Status {
    ACTIVE
  }

  fun encoded(encoder: PasswordEncoder) = Party(
    id = id,
    username = username,
    password = encoder.encode(password),
    role = role,
    status = status,
  )
}

fun fromEntity(entity: PartyEntity) = Party(
  id = entity.id,
  username = entity.username,
  password = entity.password,
  role = Party.Role.valueOf(entity.role.name),
  status = Party.Status.valueOf(entity.status.name),
)

fun toEntity(party: Party) = PartyEntity(
  id = party.id,
  username = party.username,
  password = party.password,
  role = PartyEntity.Role.valueOf(party.role.name),
  status = PartyEntity.Status.valueOf(party.status.name),
)
