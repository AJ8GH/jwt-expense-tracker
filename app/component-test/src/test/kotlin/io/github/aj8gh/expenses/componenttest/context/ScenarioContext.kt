package io.github.aj8gh.expenses.componenttest.context

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ScenarioContext(
  var responseEntity: ResponseEntity<String>? = null,
)
