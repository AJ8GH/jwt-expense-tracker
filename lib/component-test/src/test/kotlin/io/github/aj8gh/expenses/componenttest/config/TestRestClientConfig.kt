package io.github.aj8gh.expenses.componenttest.config

import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.client.RestClient

@Lazy
@TestConfiguration
class TestRestClientConfig {

  @Bean
  fun client(restClient: RestClient, scenarioContext: ScenarioContext) =
    Client(restClient, scenarioContext)

  @Bean
  fun testRestClient(
    @LocalServerPort port: Int,
    @Value("\${server.servlet.context-path}") contextPath: String,
    builder: RestClient.Builder,
  ): RestClient = builder
    .baseUrl("http://localhost:$port$contextPath")
    .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
    .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
    .build()
}
