package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.kotest.matchers.shouldBe

class AuthSteps(
  private val scenarioContext: ScenarioContext,
  private val client: Client,
) {

  @When("the following auth request is made")
  fun sendAuthRequest(request: AuthenticationRequest) {
    val response = client.post(
      path = AUTH_PATH,
      content = request,
      responseType = String::class.java
    )
    scenarioContext.responseEntity = response
  }

  @Then("the response status is {int}")
  fun responseStatusIs(expected: Int) = scenarioContext
    .responseEntity!!
    .statusCode.value() shouldBe expected
}
