package io.github.aj8gh.expenses.componenttest.context

import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.business.model.auth.AuthenticationResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ScenarioContext(
  var responseEntity: ResponseEntity<*>? = null,
  var partyAuthRequests: MutableMap<UUID, AuthenticationRequest> = mutableMapOf(),
  var partyAuthResponses: MutableMap<UUID, AuthenticationResponse> = mutableMapOf(),
) {

  fun <T> body() = responseEntity!!.body as T
  fun authRequest(party: UUID) = partyAuthRequests[party]!!
  fun accessToken(party: UUID) = partyAuthResponses[party]!!.accessToken
  fun refreshToken(party: UUID) = partyAuthResponses[party]!!.refreshToken
}
