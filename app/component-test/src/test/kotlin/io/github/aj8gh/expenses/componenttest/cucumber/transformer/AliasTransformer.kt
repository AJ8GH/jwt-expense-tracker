package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.ParameterType
import io.github.aj8gh.expenses.componenttest.context.Aliases

class AliasTransformer(
  private val aliases: Aliases,
) {

  @ParameterType(value = "\\w+")
  fun alias(alias: String) = aliases.getOrCreate(alias)
}
