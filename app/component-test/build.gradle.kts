import io.github.aj8gh.expenses.plugin.BUSINESS_MODULE
import io.github.aj8gh.expenses.plugin.PERSISTENCE_MODULE
import io.github.aj8gh.expenses.plugin.PRESENTATION_MODULE
import io.github.aj8gh.expenses.plugin.moduleId

plugins {
  alias(libs.plugins.kotlin.jpa)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  testImplementation("net.java.dev.jna:jna:5.17.0")
  testImplementation(platform(libs.cucumber.bom))
  testImplementation(project(moduleId(PERSISTENCE_MODULE)))
  testImplementation(project(moduleId(PRESENTATION_MODULE)))
  testImplementation(project(moduleId(BUSINESS_MODULE)))
  testImplementation(rootProject.libs.bundles.common)
  testImplementation(rootProject.libs.bundles.api)
  testImplementation(rootProject.libs.bundles.service)
  testImplementation(rootProject.libs.bundles.persistence)
  testImplementation(rootProject.libs.bundles.componentTest)
  testRuntimeOnly(rootProject.libs.bundles.runtimeOnly)
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(tasks.koverVerify)
}

tasks.bootJar {
  enabled = false
}
