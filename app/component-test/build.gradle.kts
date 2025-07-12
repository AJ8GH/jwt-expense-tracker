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
  testImplementation(project(moduleId("persistence")))
  testImplementation(project(moduleId("presentation")))
  testImplementation(project(moduleId("business")))
  testImplementation(rootProject.libs.bundles.common)
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
