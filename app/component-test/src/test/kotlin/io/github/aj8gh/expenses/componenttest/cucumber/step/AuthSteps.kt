package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.REFRESH_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.kotest.matchers.nulls.shouldNotBeNull
import java.util.*

class AuthSteps(
  private val scenarioContext: ScenarioContext,
  private val client: Client,
  private val partySteps: PartySteps,
) {

  @Given("party {string} is authenticated with tokens: access {string}, refresh {string}")
  fun partyIsAuthenticated(alias: String, access: String, refresh: String) {
    val party = partySteps.createParty(alias).id
    val authResponse = authenticate(party)
    scenarioContext.tokens[access] = authResponse.accessToken
    scenarioContext.tokens[refresh] = authResponse.refreshToken
  }

  @When("an auth request is made for {alias}")
  fun sendAuthRequest(party: UUID) = authenticate(party)

  @When("an auth refresh request is made for {alias} returning access token {string}")
  fun sendAuthRefreshRequest(party: UUID, alias: String) = refresh(party)
    .let { scenarioContext.tokens[alias] = it.accessToken }

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
  ).let { it.body!! }
    .also { scenarioContext.partyAuthResponses[party] = it }

  private fun refresh(party: UUID) = client.post(
    path = "$AUTH_PATH$REFRESH_PATH",
    content = RefreshTokenRequest(scenarioContext.refreshToken(party)),
    responseType = RefreshTokenResponse::class,
  ).let { it.body!! }
    .also { scenarioContext.partyRefreshResponses[party] = it }
}
