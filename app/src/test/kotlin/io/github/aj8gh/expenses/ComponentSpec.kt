package io.github.aj8gh.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.api.controller.AUTH_PATH
import io.github.aj8gh.expenses.api.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.client.AppClient
import io.github.aj8gh.expenses.config.TestClientConfig
import io.github.aj8gh.expenses.config.TestDatabaseConfig
import io.github.aj8gh.expenses.persistence.model.PersonEntity
import io.github.aj8gh.expenses.persistence.repository.PersonRepository
import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

private const val USERNAME = "username"
private const val PASSWORD = "password"

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
  private val httpClient: OkHttpClient,
  private val mapper: ObjectMapper,
  private val mockMvc: MockMvc,
  private val personRepository: PersonRepository,
  @LocalServerPort private val port: Int,
  @Value("\${server.servlet.context-path}") private val basePath: String,
) : FunSpec({

  val client = AppClient(httpClient, mapper, port, basePath)
  val user = PersonEntity(username = USERNAME, password = PASSWORD)
  val authRequest = AuthenticationRequest(username = USERNAME, password = PASSWORD)

  fun saveUser() = personRepository.save(user)

  fun performPost(path: String, content: Any) = mockMvc.perform(
    post(path)
      .contentType(APPLICATION_JSON)
      .content(mapper.writeValueAsString(content))
  )

  test("should allow authentication and provide access and refresh tokens") {
    saveUser()
    performPost(AUTH_PATH, authRequest)
      .andExpect(status().isCreated)
      .andExpect(jsonPath("$.accessToken").isNotEmpty)
      .andExpect(jsonPath("$.refreshToken").isNotEmpty)
  }

  test("auth request for unknown user should return 403") {
    performPost(AUTH_PATH, authRequest)
      .andExpect(status().isForbidden)
      .andExpect(jsonPath("$.accessToken").isEmpty)
      .andExpect(jsonPath("$.refreshToken").isEmpty)
  }
})
