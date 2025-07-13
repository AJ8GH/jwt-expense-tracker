package io.github.aj8gh.expenses.componenttest

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.persistence.model.PartyEntity.Role.USER
import io.github.aj8gh.expenses.persistence.model.PartyEntity.Status.ACTIVE
import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
import io.github.aj8gh.expenses.persistence.repository.JpaRefreshTokenRepository
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PartySpec(
  override val mockMvc: MockMvc,
  override val mapper: ObjectMapper,
  private val refreshTokenRepository: JpaRefreshTokenRepository,
  private val partyRepository: JpaPartyRepository,
) : ComponentSpec(mockMvc, mapper) {

  init {
    beforeEach {
      partyRepository.deleteAll()
      refreshTokenRepository.deleteAll()
    }

    this.test("should create party") {
      performPost(PARTIES_PATH, createPartyRequest())
        .andExpect(status().isCreated)

      partyRepository.findByUsername(USERNAME)!!.let {
        it.username shouldBeEqual USERNAME
        it.status shouldBeEqual ACTIVE
        it.role shouldBeEqual USER
        shouldNotBeNull {
          it.password
          it.createdAt
          it.updatedAt
        }
      }
    }

    this.test("should not create when username exists") {
      authenticate()
      performPost(PARTIES_PATH, createPartyRequest())
        .andExpect(status().isConflict)
    }

    this.test("should get when username exists and token valid for user") {
      // TODO
    }

    this.test("should return 404 get when username does not exist") {
      // TODO
    }

    this.test("should return 401 get when token not valid for user") {
      // TODO
    }
  }
}
