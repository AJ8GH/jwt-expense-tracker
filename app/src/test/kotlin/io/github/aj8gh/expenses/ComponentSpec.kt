package io.github.aj8gh.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.aj8gh.expenses.client.AppClient
import io.github.aj8gh.expenses.config.TestClientConfig
import io.github.aj8gh.expenses.config.TestDatabaseConfig
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import okhttp3.Headers.Companion.headersOf
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.Base64.getEncoder

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
  @LocalServerPort private val port: Int,
  @Value("\${spring.security.user.name}") user: String,
  @Value("\${spring.security.user.password}") password: String,
) : FunSpec({

  val client = AppClient(httpClient, mapper, port)

  test("should accept healthcheck request without authentication") {
    client.get("/actuator/health")
      .also { it.code shouldBe 200 }
      .also { it.body.string() shouldContain "UP" }
  }

  test("should reject info request without authentication") {
    client.get("/actuator/info")
      .code shouldBe 401
  }

  test("should allow authenticated info request") {
    val encodedAuth = getEncoder().encodeToString("$user:$password".toByteArray())
    client.get("/actuator/info", headersOf("Authorization", "Basic $encodedAuth"))
      .code shouldBe 200
  }
})
