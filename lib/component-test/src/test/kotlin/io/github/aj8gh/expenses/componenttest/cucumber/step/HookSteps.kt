package io.github.aj8gh.expenses.componenttest.cucumber.step

import io.cucumber.java.Before
import org.springframework.data.jpa.repository.JpaRepository

class HookSteps(
  private val repositories: List<JpaRepository<*, *>>,
) {

  @Before
  fun before() = repositories.forEach(JpaRepository<*, *>::deleteAll)
}
