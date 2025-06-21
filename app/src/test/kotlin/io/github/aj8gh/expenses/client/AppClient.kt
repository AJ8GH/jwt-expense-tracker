package io.github.aj8gh.expenses.client

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component

@Component
class AppClient(
  private val client: OkHttpClient,
  private val mapper: ObjectMapper,
  @LocalServerPort private val port: Int,
) {

  fun get(path: String): Response {
    val request = Request.Builder()
      .url("http://localhost:$port/api$path")
      .build()

    return client.newCall(request).execute()
  }

  fun get(path: String, headers: Headers): Response {
    val request = Request.Builder()
      .url("http://localhost:$port/api$path")
      .headers(headers)
      .build()

    return client.newCall(request).execute()
  }

  fun <T> post(path: String, requestBody: T): Response {
    val request = Request.Builder()
      .url("http://localhost:$port/api$path")
      .post(mapper.writeValueAsString(requestBody).toRequestBody(APPLICATION_JSON_VALUE.toMediaType()))
      .build()

    return client.newCall(request).execute()
  }
}
