package io.github.aj8gh.expenses.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity(name = "expense")
data class ExpenseEntity(
  @Id @UuidGenerator val id: UUID? = null,
  val partyId: UUID,
  @Enumerated(STRING) val category: Category,
  val amount: BigDecimal,
  val date: LocalDate,
  val description: String? = null,
  @CreationTimestamp val createdAt: Instant? = null,
  @UpdateTimestamp val updatedAt: Instant? = null,
) {

  enum class Category {
    FOOD,
    BILLS,
    MORTGAGE,
    RENT,
    ENTERTAINMENT,
    TRAVEL,
    CLOTHING,
    HOME_MAINTENANCE,
    OTHER,
  }
}
