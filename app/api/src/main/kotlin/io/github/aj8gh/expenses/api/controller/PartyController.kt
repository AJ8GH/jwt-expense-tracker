package io.github.aj8gh.expenses.api.controller

import io.github.aj8gh.expenses.api.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.api.model.party.fromRequest
import io.github.aj8gh.expenses.api.model.party.toResponse
import io.github.aj8gh.expenses.constant.PARTIES_PATH
import io.github.aj8gh.expenses.service.party.PartyService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PARTIES_PATH)
class PartyController(
  private val service: PartyService,
) {

  @PostMapping
  @ResponseStatus(CREATED)
  fun create(
    @RequestBody request: CreatePartyRequest,
  ) = toResponse(service.create(fromRequest(request)))
}
