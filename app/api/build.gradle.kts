import io.github.aj8gh.expenses.plugin.BUSINESS_MODULE
import io.github.aj8gh.expenses.plugin.moduleId

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(project(moduleId(BUSINESS_MODULE)))
  implementation(rootProject.libs.bundles.api)
  implementation(rootProject.libs.bundles.common)
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}

tasks.bootJar {
  enabled = false
}
