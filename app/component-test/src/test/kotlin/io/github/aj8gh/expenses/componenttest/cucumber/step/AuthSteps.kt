package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.REFRESH_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenRequest
import io.github.aj8gh.expenses.business.model.auth.RefreshTokenResponse
import io.github.aj8gh.expenses.business.service.security.JwtUserDetailsService
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.kotest.matchers.nulls.shouldNotBeNull
import org.mockito.Mockito.`when`
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import java.util.*

class AuthSteps(
  private val scenarioContext: ScenarioContext,
  private val client: Client,
  private val partySteps: PartySteps,
  @MockitoSpyBean private val userDetailsService: JwtUserDetailsService,
) {

  @Given("party {string} is authenticated with tokens: access {string}, refresh {string}")
  fun partyIsAuthenticated(alias: String, access: String, refresh: String) {
    val party = partySteps.createParty(alias).id
    val authResponse = authenticate(party)
    scenarioContext.tokens[access] = authResponse.accessToken
    scenarioContext.tokens[refresh] = authResponse.refreshToken
  }

  @Given("user details service returns username {string} password {string} for {alias}")
  fun userDetailsServiceReturns(
    username: String,
    password: String,
    party: UUID,
  ) = scenarioContext.authRequest(party).username.let {
    `when`(userDetailsService.loadUserByUsername(it))
      .thenReturn(User(username, password, listOf()))
  }

  @When("the following auth request is made")
  fun sendAuthRequest(request: AuthenticationRequest) = authenticate(request)

  @When("an auth request is made for {alias}")
  fun sendAuthRequest(party: UUID) = authenticate(party)

  @When("an auth refresh request is made for {alias} returning access token {string}")
  fun sendAuthRefreshRequest(party: UUID, alias: String) = refresh(party)
    .also { scenarioContext.partyRefreshResponses[party] = it }
    .let { scenarioContext.tokens[alias] = it.accessToken }

  @When("an auth refresh request is made with token {tokenAlias}")
  fun sendAuthRefreshRequestWithToken(token: String) = refresh(token = token)

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
  ).body!!.also { scenarioContext.partyAuthResponses[party] = it }

  private fun authenticate(request: AuthenticationRequest) = client.post(
    path = AUTH_PATH,
    content = request,
    responseType = String::class,
  ).body!!

  private fun refresh(party: UUID) = client.post(
    path = "$AUTH_PATH$REFRESH_PATH",
    content = RefreshTokenRequest(scenarioContext.refreshToken(party)),
    responseType = RefreshTokenResponse::class,
  ).body!!

  private fun refresh(token: String) = client.post(
    path = "$AUTH_PATH$REFRESH_PATH",
    content = RefreshTokenRequest(token),
    responseType = String::class,
  ).body!!
}
