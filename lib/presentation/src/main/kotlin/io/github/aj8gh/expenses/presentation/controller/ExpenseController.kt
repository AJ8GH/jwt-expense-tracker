package io.github.aj8gh.expenses.presentation.controller

import io.github.aj8gh.expenses.business.constant.EXPENSES_PATH
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EXPENSES_PATH)
class ExpenseController {

  @GetMapping
  fun findAll() = """{"expenses": []}""" // TODO
}
