package io.github.aj8gh.expenses.api.controller

import io.github.aj8gh.expenses.api.model.RegistrationRequest
import io.github.aj8gh.expenses.persistence.repository.PersonRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

const val REGISTRATION_PATH = "/register"

@RestController
class RegistrationController(private val repository: PersonRepository) {

  @PostMapping(REGISTRATION_PATH)
  fun create(@RequestBody request: RegistrationRequest) = "Hello"
}
