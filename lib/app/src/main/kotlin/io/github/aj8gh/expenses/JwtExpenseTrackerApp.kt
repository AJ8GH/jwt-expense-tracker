package io.github.aj8gh.expenses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.aj8gh.expenses.*"])
class JwtExpenseTrackerApp

fun main(args: Array<String>) {
  runApplication<JwtExpenseTrackerApp>(*args)
}
