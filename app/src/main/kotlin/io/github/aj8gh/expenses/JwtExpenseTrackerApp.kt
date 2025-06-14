package io.github.aj8gh.expenses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtExpenseTrackerApp

fun main(args: Array<String>) {
  runApplication<JwtExpenseTrackerApp>(*args)
}
