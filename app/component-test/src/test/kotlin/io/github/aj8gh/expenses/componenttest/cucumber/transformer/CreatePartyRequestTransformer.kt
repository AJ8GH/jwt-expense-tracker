package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.DataTableType
import io.github.aj8gh.expenses.componenttest.constant.DEFAULT_PASSWORD
import io.github.aj8gh.expenses.componenttest.constant.DEFAULT_USERNAME
import io.github.aj8gh.expenses.presentation.model.party.CreatePartyRequest
import io.github.aj8gh.expenses.presentation.model.party.Role
import io.github.aj8gh.expenses.presentation.model.party.Role.USER

private const val USERNAME = "username"
private const val PASSWORD = "password"
private const val ROLE = "role"

class CreatePartyRequestTransformer {

  @DataTableType
  fun transform(data: Map<String, String>) = CreatePartyRequest(
    username = data[USERNAME] ?: DEFAULT_USERNAME,
    password = data[PASSWORD] ?: DEFAULT_PASSWORD,
    role = data[ROLE]?.let(Role::valueOf) ?: USER,
  )
}
