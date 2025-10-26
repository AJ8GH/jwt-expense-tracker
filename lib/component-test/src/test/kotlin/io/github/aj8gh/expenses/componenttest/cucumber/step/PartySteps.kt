package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext
import io.github.aj8gh.expenses.componenttest.cucumber.transformer.CreatePartyRequestTransformer
import io.github.aj8gh.expenses.componenttest.rest.Client
import io.github.aj8gh.expenses.persistence.repository.JpaPartyRepository
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.presentation.model.party.PartyResponse
import io.github.aj8gh.expenses.presentation.model.party.Role
import io.github.aj8gh.expenses.presentation.model.party.Status
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class PartySteps(
  private val client: Client,
  private val aliases: Aliases,
  private val transformer: CreatePartyRequestTransformer,
  private val repository: JpaPartyRepository,
  private val scenarioContext: ScenarioContext,
) {

  @Given("party {string} is created")
  fun partyExists(alias: String) = createParty(alias)

  @Given("the following create party request is sent")
  fun partyExists(request: CreatePartyRequest) = createParty(request)

  @Given("party {alias} is deleted")
  fun deleteParty(id: UUID) = repository.deleteById(id)

  @Then("{alias} is stored")
  fun partyIsStored(party: UUID) {
    repository.findById(party).isPresent shouldBe true
  }

  @Then("{alias} is returned")
  fun partyIsReturned(party: UUID) {
    val actual = repository.findById(party).map {
      PartyResponse(
        id = it.id!!,
        username = it.username,
        password = it.password,
        role = Role.valueOf(it.role.name),
        status = Status.valueOf(it.status.name)
      )
    }.orElseThrow()
    scenarioContext.body<PartyResponse>() shouldBeEqual actual
  }

  fun createParty(
    alias: String,
    request: CreatePartyRequest = transformer.default(alias),
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

  fun createParty(request: CreatePartyRequest) = client.post(
    path = PARTIES_PATH,
    content = request,
    responseType = String::class
  ).body!!
}
