package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.cucumber.transformer.CreatePartyRequestTransformer
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.presentation.model.party.PartyResponse

class PartySteps(
  private val client: Client,
  private val aliases: Aliases,
  private val transformer: CreatePartyRequestTransformer,
  private val scenarioContext: ScenarioContext,
) {

  @Given("party {string} is created")
  fun partyExists(alias: String) = createParty(alias)

  @Given("the following party {string} is created")
  fun partyExists(alias: String, request: CreatePartyRequest) =
    createParty(alias, request)

  fun createParty(
    alias: String,
    request: CreatePartyRequest = transformer.default(),
  ): PartyResponse {
    val created = client.post(
      path = PARTIES_PATH,
      content = request,
      responseType = PartyResponse::class
    ).body!!
    aliases.put(alias, created.id)
    scenarioContext.partyAuthRequests[created.id] = AuthenticationRequest(
      username = request.username,
      password = request.password,
    )
    return created
  }
}
