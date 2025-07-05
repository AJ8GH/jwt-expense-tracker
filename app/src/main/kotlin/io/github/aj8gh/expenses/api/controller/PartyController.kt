package io.github.aj8gh.expenses.api.controller

import io.github.aj8gh.expenses.api.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.persistence.model.PartyEntity
import io.github.aj8gh.expenses.persistence.repository.PartyRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val PARTIES_PATH = "/parties"

@RestController
@RequestMapping(PARTIES_PATH)
class PartyController(
  private val partyRepository: PartyRepository,
) {

  @PostMapping
  fun create(
    @RequestBody request: CreatePartyRequest,
  ) = partyRepository.save(
    PartyEntity(
      username = request.username,
      password = request.password,
      role = request.role,
    )
  )
}
