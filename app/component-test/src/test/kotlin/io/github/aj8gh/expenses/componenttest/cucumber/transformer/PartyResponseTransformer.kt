package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.DataTableType
import io.github.aj8gh.expenses.componenttest.constant.ID
import io.github.aj8gh.expenses.componenttest.constant.PASSWORD
import io.github.aj8gh.expenses.componenttest.constant.ROLE
import io.github.aj8gh.expenses.componenttest.constant.STATUS
import io.github.aj8gh.expenses.componenttest.constant.USERNAME
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.presentation.model.party.PartyResponse
import io.github.aj8gh.expenses.presentation.model.party.Role
import io.github.aj8gh.expenses.presentation.model.party.Status

class PartyResponseTransformer(
  private val aliases: Aliases,
) {

  @DataTableType
  fun transform(data: Map<String, String>) = PartyResponse(
    id = aliases.getOrCreate(data[ID]!!),
    username = data[USERNAME]!!,
    password = data[PASSWORD]!!,
    role = Role.valueOf(data[ROLE]!!),
    status = Status.valueOf(data[STATUS]!!),
  )
}
