package io.github.aj8gh.expenses.componenttest.rest

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION
import io.github.aj8gh.expenses.business.service.security.BEARER
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.RequestHeadersSpec
import kotlin.reflect.KClass

class Client(
  private val restClient: RestClient,
  private val scenarioContext: ScenarioContext,
) {

  fun <T : Any> post(
    path: String,
    content: Any,
    responseType: KClass<T>,
    token: String? = null,
  ) = restClient.post()
    .uri(path)
    .body(content)
    .let { makeRequest(it, token, responseType) }

  fun <T : Any> get(
    path: String,
    responseType: KClass<T>,
    token: String? = null,
  ) = restClient.get()
    .uri(path)
    .let { makeRequest(it, token, responseType) }

  private fun <T : Any> makeRequest(
    spec: RequestHeadersSpec<*>,
    token: String?,
    responseType: KClass<T>,
  ) = withTokenIfPresent(spec, token)
    .retrieve()
    .toEntity(responseType.java)
    .also { scenarioContext.responseEntity = it }

  private fun withTokenIfPresent(spec: RequestHeadersSpec<*>, token: String?) =
    spec.let {
      token?.let { token ->
        it.header(AUTHORIZATION, "$BEARER$token")
      } ?: it
    }
}
