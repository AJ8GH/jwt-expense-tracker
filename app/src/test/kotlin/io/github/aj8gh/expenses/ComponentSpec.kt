package io.github.aj8gh.expenses

import io.github.aj8gh.expenses.config.TestClientConfig
import io.github.aj8gh.expenses.config.TestDatabaseConfig
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(type = POSTGRES, provider = ZONKY)
@Import(value = [TestClientConfig::class, TestDatabaseConfig::class])
class ComponentSpec(
  private val client: OkHttpClient,
  @LocalServerPort private val port: Int,
) : FunSpec({

  test("should load context") {
    val request = Request.Builder()
      .url("http://localhost:$port/actuator/health")
      .build()

    client.newCall(request)
      .execute()
      .isSuccessful shouldBe true
  }
})
