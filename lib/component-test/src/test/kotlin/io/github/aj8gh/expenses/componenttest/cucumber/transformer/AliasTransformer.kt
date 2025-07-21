package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.ParameterType
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.componenttest.context.ScenarioContext

private const val NULL = "null"

class AliasTransformer(
  private val aliases: Aliases,
  private val scenarioContext: ScenarioContext,
) {

  @ParameterType(value = "\\w+")
  fun alias(alias: String) = aliases.getOrCreate(alias)

  @ParameterType(value = "\\w+")
  fun tokenAlias(alias: String) =
    if (alias == NULL) null else scenarioContext.token(alias)
}
