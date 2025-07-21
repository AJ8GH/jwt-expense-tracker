import io.github.aj8gh.expenses.plugin.BUSINESS_MODULE
import io.github.aj8gh.expenses.plugin.PERSISTENCE_MODULE
import io.github.aj8gh.expenses.plugin.PRESENTATION_MODULE
import io.github.aj8gh.expenses.plugin.moduleId
import io.github.aj8gh.expenses.plugin.prop

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(project(moduleId(PERSISTENCE_MODULE)))
  implementation(project(moduleId(BUSINESS_MODULE)))
  implementation(project(moduleId(PRESENTATION_MODULE)))
  implementation(rootProject.libs.bundles.service)
  implementation(rootProject.libs.bundles.common)
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}

tasks.bootJar {
  mainClass = prop("project.main-class")
}
