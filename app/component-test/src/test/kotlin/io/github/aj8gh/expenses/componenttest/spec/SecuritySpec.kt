// package io.github.aj8gh.expenses.componenttest.spec
//
// import com.fasterxml.jackson.databind.ObjectMapper
// import io.github.aj8gh.expenses.business.constant.AUTH_PATH
// import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
// import io.github.aj8gh.expenses.business.constant.REFRESH_PATH
// import io.github.aj8gh.expenses.business.service.security.JwtService
// import io.github.aj8gh.expenses.business.service.security.JwtUserDetailsService
// import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
// import io.github.aj8gh.expenses.persistence.repository.JpaRefreshTokenRepository
// import io.github.aj8gh.expenses.presentation.model.auth.RefreshTokenRequestDto
// import io.github.aj8gh.expenses.presentation.model.auth.RefreshTokenResponseDto
// import io.jsonwebtoken.ExpiredJwtException
// import org.mockito.Mockito.reset
// import org.mockito.Mockito.`when`
// import org.springframework.security.core.userdetails.User
// import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
// import org.springframework.test.web.servlet.MockMvc
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//
// class SecuritySpec(
//   override val mockMvc: MockMvc,
//   override val mapper: ObjectMapper,
//   @MockitoSpyBean private val jwtService: JwtService,
//   @MockitoSpyBean private val userDetailsService: JwtUserDetailsService,
//   private val jpaRefreshTokenRepository: JpaRefreshTokenRepository,
//   private val jpaPartyRepository: JpaPartyRepository,
// ) : ComponentSpec(mockMvc, mapper) {
//
//   init {
//     beforeEach {
//       jpaPartyRepository.deleteAll()
//       jpaRefreshTokenRepository.deleteAll()
//       reset(userDetailsService)
//       reset(jwtService)
//     }
//
//     this.test("authentication request from existing user should return 201 with tokens") {
//       createParty()
//       performPost(AUTH_PATH, authRequest())
//         .andExpect(status().isCreated)
//         .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
//         .andExpect(jsonPath(REFRESH_TOKEN_PATH).isNotEmpty)
//     }
//
//     this.test("authenticated request should return 200") {
//       val authResponse = authenticate()
//       performGet(EXPENSES_PATH, authResponse.accessToken)
//         .andExpect(status().isOk)
//     }
//
//     this.test("refresh request with valid token should return 201 with new access token") {
//       val authResponse = authenticate()
//       performGet(EXPENSES_PATH, authResponse.accessToken)
//         .andExpect(status().isOk)
//
//       `when`(jwtService.extractClaims(authResponse.accessToken))
//         .thenThrow(ExpiredJwtException::class.java)
//
//       performGet(EXPENSES_PATH, authResponse.accessToken)
//         .andExpect(status().isForbidden)
//
//       reset(jwtService)
//
//       val refreshResponse = refreshJwt(authResponse.refreshToken)
//       performGet(EXPENSES_PATH, refreshResponse.accessToken)
//         .andExpect(status().isOk)
//     }
//
//     this.test("unauthenticated request to secure endpoint should return 403") {
//       performGet(EXPENSES_PATH)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("using refresh token as access token should return 403") {
//       val authResponse = authenticate()
//       performGet(EXPENSES_PATH, authResponse.refreshToken)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("using access token as refresh token should return 403") {
//       val authResponse = authenticate()
//       performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(authResponse.accessToken))
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("auth request for unknown user should return 403") {
//       performPost(AUTH_PATH, authRequest())
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with access token for non-existent user should return 403") {
//       performGet(EXPENSES_PATH, INVALID_ACCESS_TOKEN)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with refresh token for non-existent user should return 403") {
//       performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(INVALID_REFRESH_TOKEN))
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with tampered access token should return 403") {
//       val authResponse = authenticate()
//       `when`(userDetailsService.loadUserByUsername(USERNAME))
//         .thenReturn(User(OTHER_USERNAME, OTHER_PASSWORD, listOf()))
//       performGet(EXPENSES_PATH, authResponse.accessToken)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with tampered refresh token should return 403") {
//       val authResponse = authenticate()
//       `when`(userDetailsService.loadUserByUsername(USERNAME))
//         .thenReturn(User(OTHER_USERNAME, OTHER_PASSWORD, listOf()))
//       performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(authResponse.refreshToken))
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with invalid bearer token format should return 403") {
//       val authResponse = authenticate()
//       performGet(EXPENSES_PATH, INVALID_TOKEN_PREFIX, authResponse.accessToken)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with expired access token should return 403") {
//       authenticate()
//       performGet(EXPENSES_PATH, EXPIRED_ACCESS_TOKEN)
//         .andExpect(status().isForbidden)
//     }
//
//     this.test("request with expired refresh token should return 403") {
//       authenticate()
//       performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(EXPIRED_REFRESH_TOKEN))
//         .andExpect(status().isForbidden)
//     }
//   }
//
//   fun refreshJwt(refreshToken: String): RefreshTokenResponseDto =
//     performPost("$AUTH_PATH$REFRESH_PATH", RefreshTokenRequestDto(refreshToken))
//       .andExpect(status().isCreated)
//       .andExpect(jsonPath(ACCESS_TOKEN_PATH).isNotEmpty)
//       .andReturn()
//       .response
//       .contentAsString
//       .let { mapper.readValue(it, RefreshTokenResponseDto::class.java) }
// }
//
// const val USERNAME = "username"
// const val PASSWORD = "password"
// const val ACCESS_TOKEN_PATH = "$.accessToken"
// const val REFRESH_TOKEN_PATH = "$.refreshToken"
//
// private const val OTHER_USERNAME = "some-other-username"
// private const val OTHER_PASSWORD = "some-other-password"
// private const val INVALID_TOKEN_PREFIX = "Beerer "
// private const val INVALID_ACCESS_TOKEN =
//   "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJzdWIiOiJtZTgiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6MTc1MTc1NjQ3MH0.174KlSzPRTEvkAY0md5S4psUn6hHXFJ53KgdasqrPHU"
// private const val INVALID_REFRESH_TOKEN =
//   "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwic3ViIjoic29tZU90aGVyVXNlck5hbWUiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6OTk5OTk5OTk5OX0.nHZaaooKZgXL8WTBP7dwEFIHRsWMeeaEaGZOQYLL_F4"
// private const val EXPIRED_ACCESS_TOKEN =
//   "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc1MTc1NjE3MCwiZXhwIjoxNzUxNzU2MTcxfQ.O8pEbFjXt1XAoegBQhrKZH7GitGVnHY7DjZObzEMcDk"
// private const val EXPIRED_REFRESH_TOKEN =
//   "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwic3ViIjoidXNlcm5hbWUiLCJpYXQiOjE3NTE3NTYxNzAsImV4cCI6MTc1MTc1NjE3MX0.JPDKVsJwRbURN2jk8RXk4XIby4ETT0j4lgNGuDn1Idc"
