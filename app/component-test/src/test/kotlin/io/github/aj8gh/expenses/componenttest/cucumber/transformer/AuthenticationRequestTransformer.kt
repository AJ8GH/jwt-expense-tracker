package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.DataTableType
import io.github.aj8gh.expenses.business.model.auth.AuthenticationRequest
import io.github.aj8gh.expenses.componenttest.constant.DEFAULT_PASSWORD
import io.github.aj8gh.expenses.componenttest.constant.DEFAULT_USERNAME

private const val USERNAME = "username"
private const val PASSWORD = "password"

class AuthenticationRequestTransformer {

  @DataTableType
  fun transform(data: Map<String, String>) = AuthenticationRequest(
    username = data[USERNAME] ?: DEFAULT_USERNAME,
    password = data[PASSWORD] ?: DEFAULT_PASSWORD,
  )
}
