package io.github.aj8gh.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.api.controller.AUTH_PATH
import io.github.aj8gh.expenses.api.controller.REFRESH_PATH
import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.api.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.config.TestClientConfig
import io.github.aj8gh.expenses.config.TestDatabaseConfig
import io.github.aj8gh.expenses.persistence.model.PersonEntity
import io.github.aj8gh.expenses.persistence.repository.JpaPersonRepository
import io.github.aj8gh.expenses.persistence.repository.PersonRepository
import io.github.aj8gh.expenses.service.security.JwtService
import io.jsonwebtoken.ExpiredJwtException
import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

private const val USERNAME = "username"
private const val PASSWORD = "password"
private const val BEARER = "Bearer"
private const val INFO_PATH = "/actuator/info"

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(type = POSTGRES, provider = ZONKY)
@Import(
  value = [
    TestClientConfig::class,
    TestDatabaseConfig::class,
  ]
)
class ComponentSpec(
  @Value("\${server.servlet.context-path}") private val basePath: String,
  @MockitoSpyBean private val jwtService: JwtService,
  private val mapper: ObjectMapper,
  private val mockMvc: MockMvc,
  private val personRepository: PersonRepository,
  private val jpaPersonRepository: JpaPersonRepository,
) : FunSpec({

  val user = PersonEntity(username = USERNAME, password = PASSWORD)
  val authRequest = AuthenticationRequest(username = USERNAME, password = PASSWORD)

  fun saveUser() = personRepository.save(user)

  fun performPost(path: String, content: Any) = mockMvc.perform(
    post(path)
      .contentType(APPLICATION_JSON)
      .content(mapper.writeValueAsString(content))
  )

  fun authenticate() = saveUser().let {
    performPost(AUTH_PATH, authRequest)
      .andExpect(status().isCreated)
      .andExpect(jsonPath("$.accessToken").isNotEmpty)
      .andExpect(jsonPath("$.refreshToken").isNotEmpty)
      .andReturn()
      .response
      .contentAsString
      .let { mapper.readValue(it, AuthenticationResponse::class.java) }
  }

  fun refreshJwt(refreshToken: String) =
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequest(refreshToken))
      .andExpect(status().isCreated)
      .andExpect(jsonPath("$.accessToken").isNotEmpty)
      .andReturn()
      .response
      .contentAsString
      .let { mapper.readValue(it, RefreshTokenResponse::class.java) }

  fun performGet(path: String, token: String? = null): ResultActions {
    val request = get(path).let { requestBuilder ->
      token?.let {
        requestBuilder.header(AUTHORIZATION, "$BEARER $token")
      } ?: requestBuilder
    }

    return mockMvc.perform(
      request
    )
  }

  beforeEach {
    jpaPersonRepository.deleteAll()
  }

  test("unauthenticated request to secure endpoint should return 403") {
    performGet(INFO_PATH)
      .andExpect(status().isForbidden)
  }

  test("authentication request from existing user should return 201 with tokens") {
    saveUser()
    performPost(AUTH_PATH, authRequest)
      .andExpect(status().isCreated)
      .andExpect(jsonPath("$.accessToken").isNotEmpty)
      .andExpect(jsonPath("$.refreshToken").isNotEmpty)
  }

  test("auth request for unknown user should return 403") {
    performPost(AUTH_PATH, authRequest)
      .andExpect(status().isForbidden)
  }

  test("authenticated request to secure endpoint should return 200") {
    val authResponse = authenticate()
    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isOk)
  }

  test("refresh token should produce new access token") {
    val authResponse = authenticate()
    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isOk)

    `when`(jwtService.extractUsername(authResponse.accessToken))
      .thenThrow(ExpiredJwtException::class.java)

    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isForbidden)

    reset(jwtService)

    val refreshResponse = refreshJwt(authResponse.refreshToken)
    performGet(INFO_PATH, refreshResponse.accessToken)
      .andExpect(status().isOk)
  }
})
