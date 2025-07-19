package io.github.aj8gh.expenses.componenttest.context

import org.springframework.stereotype.Component
import java.util.*
import java.util.UUID.randomUUID

@Component
class Aliases(
  private val aliases: MutableMap<String, UUID> = mutableMapOf(),
) {

  fun get(key: String) = aliases[key]

  fun put(key: String, value: UUID) = run { aliases[key] = value }

  fun getOrCreate(key: String) = aliases.computeIfAbsent(key) { randomUUID() }
}
