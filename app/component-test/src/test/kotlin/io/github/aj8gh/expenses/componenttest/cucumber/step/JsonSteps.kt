package io.github.aj8gh.expenses.componenttest.cucumber.step

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.cucumber.java.en.Then
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.kotest.matchers.shouldBe

class JsonSteps(
  private val scenarioContext: ScenarioContext,
  private val mapper: ObjectMapper,
) {

  @Then("the response json path {string} is not empty")
  fun responseJsonPathIsNotEmpty(path: String) {
    val body = scenarioContext.responseEntity!!.body
    val json = mapper.readTree(body)
    json.path(path).toString().isNotBlank() shouldBe true
  }
}
