package io.github.aj8gh.expenses.client

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class AppClient(
  private val client: OkHttpClient,
  private val mapper: ObjectMapper,
  private val port: Int,
) {

  fun get(path: String, headers: Headers = Headers.EMPTY): Response {
    return call(
      path = path,
      method = GET.name(),
      headers = headers,
    )
  }

  fun post(path: String, requestBody: Any, headers: Headers = Headers.EMPTY): Response {
    return call(
      path = path,
      method = POST.name(),
      headers = headers,
      requestBody = requestBody
    )
  }

  private fun call(
    path: String,
    method: String,
    headers: Headers,
    requestBody: Any? = null,
  ): Response {
    val body = requestBody?.let {
      mapper.writeValueAsString(it)
        .toRequestBody(APPLICATION_JSON_VALUE.toMediaType())
    }

    val request = Request.Builder()
      .url("http://localhost:$port/api$path")
      .method(method, body)
      .headers(headers)
      .build()

    return client.newCall(request).execute()
  }
}
