package io.github.aj8gh.expenses.componenttest

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.constant.REFRESH_PATH
import io.github.aj8gh.expenses.business.service.security.BEARER
import io.github.aj8gh.expenses.business.service.security.JwtService
import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
import io.github.aj8gh.expenses.persistence.repository.JpaRefreshTokenRepository
import io.github.aj8gh.expenses.presentation.model.auth.AuthenticationRequestDto
import io.github.aj8gh.expenses.presentation.model.auth.AuthenticationResponseDto
import io.github.aj8gh.expenses.presentation.model.auth.RefreshTokenRequestDto
import io.github.aj8gh.expenses.presentation.model.auth.RefreshTokenResponseDto
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.jsonwebtoken.ExpiredJwtException
import io.kotest.core.spec.style.FunSpec
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.userdetails.User
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
private const val ACCESS_TOKEN_PATH = "$.accessToken"
private const val REFRESH_TOKEN_PATH = "$.refreshToken"
private const val INVALID_TOKEN_PREFIX = "Beerer "
private const val INVALID_ACCESS_TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJzdWIiOiJtZTgiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6MTc1MTc1NjQ3MH0.174KlSzPRTEvkAY0md5S4psUn6hHXFJ53KgdasqrPHU"
private const val INVALID_REFRESH_TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwic3ViIjoic29tZU90aGVyVXNlck5hbWUiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6OTk5OTk5OTk5OX0.nHZaaooKZgXL8WTBP7dwEFIHRsWMeeaEaGZOQYLL_F4"
private const val EXPIRED_ACCESS_TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc1MTc1NjE3MCwiZXhwIjoxNzUxNzU2MTcxfQ.O8pEbFjXt1XAoegBQhrKZH7GitGVnHY7DjZObzEMcDk"
private const val EXPIRED_REFRESH_TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwic3ViIjoidXNlcm5hbWUiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6MTc1MTc1NjE3MX0.JPDKVsJwRbURN2jk8RXk4XIby4ETT0j4lgNGuDn1Idc"

class SecuritySpec(
  @MockitoSpyBean private val jwtService: JwtService,
  @MockitoSpyBean private val userDetailsService: io.github.aj8gh.expenses.business.service.security.JwtUserDetailsService,
  private val mapper: ObjectMapper,
  private val mockMvc: MockMvc,
  private val jpaPartyRepository: JpaPartyRepository,
  private val jpaRefreshTokenRepository: JpaRefreshTokenRepository,
) : ComponentSpec({

  fun authRequest(username: String = USERNAME, password: String = PASSWORD) =
    AuthenticationRequestDto(username = username, password = password)

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
        .let { mapper.readValue(it, AuthenticationResponseDto::class.java) }
    }

  fun refreshJwt(refreshToken: String) =
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(refreshToken))
      .andExpect(status().isCreated)
      .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
      .andReturn()
      .response
      .contentAsString
      .let { mapper.readValue(it, RefreshTokenResponseDto::class.java) }

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
    performGet(EXPENSES_PATH, authResponse.accessToken)
      .andExpect(status().isOk)
  }

  test("refresh request with valid token should return 201 with new access token") {
    val authResponse = authenticate()
    performGet(EXPENSES_PATH, authResponse.accessToken)
      .andExpect(status().isOk)

    `when`(jwtService.extractClaims(authResponse.accessToken))
      .thenThrow(ExpiredJwtException::class.java)

    performGet(EXPENSES_PATH, authResponse.accessToken)
      .andExpect(status().isForbidden)

    reset(jwtService)

    val refreshResponse = refreshJwt(authResponse.refreshToken)
    performGet(EXPENSES_PATH, refreshResponse.accessToken)
      .andExpect(status().isOk)
  }

  test("unauthenticated request to secure endpoint should return 403") {
    performGet(EXPENSES_PATH)
      .andExpect(status().isForbidden)
  }

  test("using refresh token as access token should return 403") {
    val authResponse = authenticate()
    performGet(EXPENSES_PATH, authResponse.refreshToken)
      .andExpect(status().isForbidden)
  }

  test("using access token as refresh token should return 403") {
    val authResponse = authenticate()
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(authResponse.accessToken))
      .andExpect(status().isForbidden)
  }

  test("auth request for unknown user should return 403") {
    performPost(AUTH_PATH, authRequest())
      .andExpect(status().isForbidden)
  }

  test("request with access token for non-existent user should return 403") {
    performGet(EXPENSES_PATH, INVALID_ACCESS_TOKEN)
      .andExpect(status().isForbidden)
  }

  test("request with refresh token for non-existent user should return 403") {
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(INVALID_REFRESH_TOKEN))
      .andExpect(status().isForbidden)
  }

  test("request with tampered access token should return 403") {
    val authResponse = authenticate()
    `when`(userDetailsService.loadUserByUsername(USERNAME))
      .thenReturn(User(OTHER_USERNAME, OTHER_PASSWORD, listOf()))
    performGet(EXPENSES_PATH, authResponse.accessToken)
      .andExpect(status().isForbidden)
  }

  test("request with tampered refresh token should return 403") {
    val authResponse = authenticate()
    `when`(userDetailsService.loadUserByUsername(USERNAME))
      .thenReturn(User(OTHER_USERNAME, OTHER_PASSWORD, listOf()))
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(authResponse.refreshToken))
      .andExpect(status().isForbidden)
  }

  test("request with invalid bearer token format should return 403") {
    val authResponse = authenticate()
    performGet(EXPENSES_PATH, INVALID_TOKEN_PREFIX, authResponse.accessToken)
      .andExpect(status().isForbidden)
  }

  test("request with expired access token should return 403") {
    authenticate()
    performGet(EXPENSES_PATH, EXPIRED_ACCESS_TOKEN)
      .andExpect(status().isForbidden)
  }

  test("request with expired refresh token should return 403") {
    authenticate()
    performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(EXPIRED_REFRESH_TOKEN))
      .andExpect(status().isForbidden)
  }
})
