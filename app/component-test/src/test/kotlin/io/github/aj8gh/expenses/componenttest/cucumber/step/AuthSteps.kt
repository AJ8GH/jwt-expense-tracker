package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.kotest.matchers.nulls.shouldNotBeNull
import java.util.*

class AuthSteps(
  private val scenarioContext: ScenarioContext,
  private val client: Client,
  private val partySteps: PartySteps,
) {

  @Given("party {string} is authenticated")
  fun partyIsAuthenticated(alias: String) {
    val party = partySteps.createParty(alias).id
    authenticate(party)
  }

  @When("an auth request is made for {alias}")
  fun sendAuthRequest(party: UUID) = authenticate(party)

  @When("authenticated GET request for {alias} is made to {string}")
  fun getRequestIsMade(party: UUID, path: String) = client.get(
    path = path,
    token = scenarioContext.accessToken(party),
    responseType = String::class
  )

  @Then("the response has access and refresh tokens")
  fun responseJsonPathIsNotEmpty() {
    val response = scenarioContext.body<AuthenticationResponse>()
    shouldNotBeNull { response.accessToken }
    shouldNotBeNull { response.refreshToken }
  }

  private fun authenticate(party: UUID) = client.post(
    path = AUTH_PATH,
    content = scenarioContext.authRequest(party),
    responseType = AuthenticationResponse::class,
  ).let {
    scenarioContext.partyAuthResponses[party] = it.body!!
  }
}
