package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.presentation.model.party.PartyResponse

class PartySteps(
  private val client: Client,
  private val aliases: Aliases,
) {

  @Given("party {string} is created")
  fun partyExists(alias: String, request: CreatePartyRequest) {
    val created = client.post(
      path = PARTIES_PATH,
      content = request,
      responseType = PartyResponse::class.java
    )
    aliases.put(alias, created.body!!.id)
  }
}
