package io.github.aj8gh.expenses.componenttest

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.service.security.BEARER
import io.github.aj8gh.expenses.componenttest.config.TestDatabaseConfig
import io.github.aj8gh.expenses.presentation.model.auth.AuthenticationRequestDto
import io.github.aj8gh.expenses.presentation.model.auth.AuthenticationResponseDto
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(type = POSTGRES, provider = ZONKY)
@Import(value = [TestDatabaseConfig::class])
abstract class ComponentSpec(
  protected val mockMvc: MockMvc,
  protected val mapper: ObjectMapper,
  body: FunSpec.() -> Unit = {},
) : FunSpec() {

  init {
    body()
  }

  fun performGet(
    path: String,
    token: String? = null,
    tokenPrefix: String = BEARER,
  ): ResultActions = get(path).let { requestBuilder ->
    token?.let {
      requestBuilder.header(AUTHORIZATION, "$tokenPrefix$token")
    } ?: requestBuilder
  }.let { mockMvc.perform(it) }

  fun performPost(
    path: String,
    content: Any,
    token: String? = null,
  ): ResultActions =
    post(path)
      .contentType(APPLICATION_JSON)
      .content(mapper.writeValueAsString(content)).let { requestBuilder ->
        token?.let {
          requestBuilder.header(AUTHORIZATION, "$BEARER$token")
        } ?: requestBuilder
      }.let {
        mockMvc.perform(it)
      }

  fun authenticate(
    username: String = USERNAME,
    password: String = PASSWORD,
  ): AuthenticationResponseDto =
    createParty(username, password).let {
      performPost(AUTH_PATH, authRequest())
        .andExpect(status().isCreated)
        .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
        .andExpect(jsonPath(REFRESH_TOKEN_PATH).isNotEmpty)
        .andReturn()
        .response
        .contentAsString
        .let { mapper.readValue(it, AuthenticationResponseDto::class.java) }
    }

  fun authRequest(username: String = USERNAME, password: String = PASSWORD) =
    AuthenticationRequestDto(username = username, password = password)

  fun createParty(
    username: String = USERNAME,
    password: String = PASSWORD,
  ): ResultActions =
    performPost(PARTIES_PATH, CreatePartyRequest(username, password))
      .andExpect(status().isCreated)

  fun createPartyRequest(
    username: String = USERNAME,
    password: String = PASSWORD,
  ) = CreatePartyRequest(username, password)
}
