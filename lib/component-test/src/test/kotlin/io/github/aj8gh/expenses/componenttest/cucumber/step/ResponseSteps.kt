package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Then
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.kotest.matchers.shouldBe

class ResponseSteps(
  private val scenarioContext: ScenarioContext,
) {

  @Then("the response status is {int}")
  fun responseStatusIs(expected: Int) = scenarioContext
    .responseEntity!!
    .statusCode.value() shouldBe expected
}
