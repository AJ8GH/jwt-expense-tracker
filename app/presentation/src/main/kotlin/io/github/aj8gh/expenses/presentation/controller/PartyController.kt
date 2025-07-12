package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.service.party.PartyService
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.presentation.model.party.fromRequest
import io.github.aj8gh.expenses.presentation.model.party.toResponse
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
