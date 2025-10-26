package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Then
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.presentation.model.error.ErrorResponse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ResponseSteps(
  private val aliases: Aliases,
  private val scenarioContext: ScenarioContext,
) {

  @Then("the response status is {int}")
  fun responseStatusIs(expected: Int) = scenarioContext
    .responseEntity!!
    .statusCode.value() shouldBe expected

  @Then("the response error message contains {string}")
  fun responseErrorMessageContains(expected: String) = scenarioContext
    .body<ErrorResponse>()
    .message shouldContain aliases.replaceAll(expected)
}
