package io.github.aj8gh.expenses

import io.github.aj8gh.expenses.config.TestClientConfig
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestClientConfig::class)
class ComponentSpec(
  private val client: OkHttpClient,
  @Value("\${local.server.port}") private val port: Int,
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
