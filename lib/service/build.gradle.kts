import io.github.aj8gh.expenses.plugin.PERSISTENCE_MODULE
import io.github.aj8gh.expenses.plugin.moduleId

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(project(moduleId(PERSISTENCE_MODULE)))
  implementation(rootProject.libs.bundles.service)
  implementation(rootProject.libs.bundles.common)
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}

tasks.bootJar {
  enabled = false
}
