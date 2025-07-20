package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.When
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import java.util.*

class RequestSteps(
  private val scenarioContext: ScenarioContext,
  private val client: Client,
) {

  @When("authenticated GET request for {alias} is made to {string}")
  fun getRequestIsMade(party: UUID, path: String) = client.get(
    path = path,
    token = scenarioContext.accessToken(party),
    responseType = String::class
  )

  @When("GET request is made to {string} with token {tokenAlias}")
  fun getRequestIsMade(path: String, token: String?) = client.get(
    path = path,
    token = token,
    responseType = String::class
  )
}
