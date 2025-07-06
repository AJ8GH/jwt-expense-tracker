package io.github.aj8gh.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.api.controller.AUTH_PATH
import io.github.aj8gh.expenses.api.controller.PARTIES_PATH
import io.github.aj8gh.expenses.api.controller.REFRESH_PATH
import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.api.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.api.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.api.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.config.TestClientConfig
import io.github.aj8gh.expenses.config.TestDatabaseConfig
import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
import io.github.aj8gh.expenses.persistence.repository.JpaRefreshTokenRepository
import io.github.aj8gh.expenses.service.security.BEARER
import io.github.aj8gh.expenses.service.security.JwtService
import io.github.aj8gh.expenses.service.user.JwtUserDetailsService
import io.jsonwebtoken.ExpiredJwtException
import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.userdetails.User
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
private const val OTHER_USERNAME = "some-other-username"
private const val OTHER_PASSWORD = "other-password"
private const val INFO_PATH = "/actuator/info"
private const val ACCESS_TOKEN_PATH = "$.accessToken"
private const val REFRESH_TOKEN_PATH = "$.refreshToken"
private const val INVALID_TOKEN_PREFIX = "Beerer "
private const val INVALID_TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwic3ViIjoibWU4IiwiaWF0IjoxNzUxNzU2MTcwLCJleHAiOjE3NTQzNDgxNzB9.XnTSk-FkviZwR98WuGEcjWZtDszJe5WTeicNXM94-E4"

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
  @MockitoSpyBean private val jwtService: JwtService,
  @MockitoSpyBean private val userDetailsService: JwtUserDetailsService,
  private val mapper: ObjectMapper,
  private val mockMvc: MockMvc,
  private val jpaPartyRepository: JpaPartyRepository,
  private val jpaRefreshTokenRepository: JpaRefreshTokenRepository,
) : FunSpec({

  fun authRequest(username: String = USERNAME, password: String = PASSWORD) =
    AuthenticationRequest(username = username, password = password)

  fun performPost(path: String, content: Any) = mockMvc.perform(
    post(path)
      .contentType(APPLICATION_JSON)
      .content(mapper.writeValueAsString(content))
  )

  fun createParty(username: String = USERNAME, password: String = PASSWORD) =
    performPost(PARTIES_PATH, CreatePartyRequest(username, password))
      .andExpect(status().isCreated)

  fun authenticate(username: String = USERNAME, password: String = PASSWORD) =
    createParty(username, password).let {
      performPost(AUTH_PATH, authRequest())
        .andExpect(status().isCreated)
        .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
        .andExpect(jsonPath(REFRESH_TOKEN_PATH).isNotEmpty)
        .andReturn()
        .response
        .contentAsString
        .let { mapper.readValue(it, AuthenticationResponse::class.java) }
    }

  fun refreshJwt(refreshToken: String) =
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequest(refreshToken))
      .andExpect(status().isCreated)
      .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
      .andReturn()
      .response
      .contentAsString
      .let { mapper.readValue(it, RefreshTokenResponse::class.java) }

  fun performGet(
    path: String,
    token: String? = null,
    tokenPrefix: String = BEARER,
  ): ResultActions = get(path).let { requestBuilder ->
    token?.let {
      requestBuilder.header(AUTHORIZATION, "$tokenPrefix$token")
    } ?: requestBuilder
  }.let { mockMvc.perform(it) }

  beforeEach {
    jpaPartyRepository.deleteAll()
    jpaRefreshTokenRepository.deleteAll()
    reset(userDetailsService)
    reset(jwtService)
  }

  test("authentication request from existing user should return 201 with tokens") {
    createParty()
    performPost(AUTH_PATH, authRequest())
      .andExpect(status().isCreated)
      .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
      .andExpect(jsonPath(REFRESH_TOKEN_PATH).isNotEmpty)
  }

  test("authenticated request should return 200") {
    val authResponse = authenticate()
    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isOk)
  }

  test("refresh request with valid token should return 201 with new access token") {
    val authResponse = authenticate()
    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isOk)

    `when`(jwtService.extractClaims(authResponse.accessToken))
      .thenThrow(ExpiredJwtException::class.java)

    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isForbidden)

    reset(jwtService)

    val refreshResponse = refreshJwt(authResponse.refreshToken)
    performGet(INFO_PATH, refreshResponse.accessToken)
      .andExpect(status().isOk)
  }

  test("unauthenticated request to secure endpoint should return 403") {
    performGet(INFO_PATH)
      .andExpect(status().isForbidden)
  }

  test("using refresh token as access token should return 403") {
    val authResponse = authenticate()
    performGet(INFO_PATH, authResponse.refreshToken)
      .andExpect(status().isForbidden)
  }

  test("auth request for unknown user should return 403") {
    performPost(AUTH_PATH, authRequest())
      .andExpect(status().isForbidden)
  }

  test("request with invalid token should return 403") {
    performGet(INFO_PATH, INVALID_TOKEN)
      .andExpect(status().isForbidden)
  }

  test("request with tampered token should return 403") {
    val authResponse = authenticate()
    `when`(userDetailsService.loadUserByUsername(USERNAME))
      .thenReturn(User(OTHER_USERNAME, OTHER_PASSWORD, listOf()))
    performGet(INFO_PATH, authResponse.accessToken)
      .andExpect(status().isForbidden)
  }

  test("request with invalid bearer token format should return 403") {
    val authResponse = authenticate()
    performGet(INFO_PATH, INVALID_TOKEN_PREFIX, authResponse.accessToken)
      .andExpect(status().isForbidden)
  }
})
