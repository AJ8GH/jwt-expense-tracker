package io.github.aj8gh.expenses.componenttest.rest

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION
import io.github.aj8gh.expenses.business.service.security.BEARER
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClient

private const val URL = "http://localhost:"

class Client(
  private val restClient: RestClient,
  private val port: Int,
  private val contextPath: String,
) {

  fun <T : Any> post(
    path: String,
    content: Any,
    token: String? = null,
    responseType: Class<T>,
  ): ResponseEntity<T> = restClient.post()
    .uri("$URL$port$contextPath$path")
    .body(content).let {
      token?.let { token ->
        it.header(AUTHORIZATION, "$BEARER$token")
      } ?: it
    }.retrieve()
    .toEntity(responseType)
}
