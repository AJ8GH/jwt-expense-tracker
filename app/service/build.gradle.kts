plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(rootProject.libs.bundles.service)
  implementation(rootProject.libs.bundles.common)
  implementation(project(":app:persistence"))
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}

tasks.test {
  useJUnitPlatform()
}
