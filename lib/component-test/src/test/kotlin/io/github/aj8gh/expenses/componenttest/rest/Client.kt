package io.github.aj8gh.expenses.componenttest.rest

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION
import io.github.aj8gh.expenses.business.service.security.BEARER
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.RequestHeadersSpec
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

class Client(
  private val restClient: RestClient,
  private val scenarioContext: ScenarioContext,
) {

  fun <T : Any> post(
    path: String,
    content: Any,
    responseType: KClass<T>,
    pathVariables: Array<Any> = emptyArray(),
    token: String? = null,
  ) = restClient.post()
    .uri(path, *pathVariables)
    .body(content)
    .let { makeRequest(it, token, responseType) }

  fun <T : Any> put(
    path: String,
    content: Any,
    responseType: KClass<T>,
    pathVariables: Array<Any> = emptyArray(),
    token: String? = null,
  ) = restClient.put()
    .uri(path, *pathVariables)
    .body(content)
    .let { makeRequest(it, token, responseType) }

  fun <T : Any> delete(
    path: String,
    responseType: KClass<T>,
    pathVariables: Array<Any> = emptyArray(),
    token: String? = null,
  ) = restClient.delete()
    .uri(path, *pathVariables)
    .let { makeRequest(it, token, responseType) }

  fun <T : Any> get(
    path: String,
    responseType: KClass<T>,
    token: String? = null,
    pathVariables: Array<Any> = emptyArray(),
    headers: Map<String, List<String>> = mapOf(),
  ) = restClient.get()
    .uri(path, *pathVariables)
    .headers { toHeaders(headers) }
    .let { makeRequest(it, token, responseType) }

  private fun <T : Any> makeRequest(
    spec: RequestHeadersSpec<*>,
    token: String?,
    responseType: KClass<T>,
  ) = withTokenIfPresent(spec, token)
    .retrieve()
    .onStatus(HttpStatusCode::isError, this::errorHandler)
    .toEntity(responseType.java)
    .also { scenarioContext.responseEntity = it }

  private fun withTokenIfPresent(spec: RequestHeadersSpec<*>, token: String?) =
    spec.let {
      token?.let { token ->
        it.header(AUTHORIZATION, "$BEARER$token")
      } ?: it
    }

  private fun errorHandler(
    req: HttpRequest,
    res: ClientHttpResponse,
  ) = logger.error {
    """Rest client HTTP error, url=${req.uri}, method=${req.method}, statusCode=${res.statusCode} : ${res.statusText}, responseBody=${res.body}"""
  }

  private fun toHeaders(headers: Map<String, List<String>>): HttpHeaders {
    return HttpHeaders(MultiValueMap.fromMultiValue(headers))
  }
}
