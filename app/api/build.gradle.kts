plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}


dependencies {
  implementation(rootProject.libs.bundles.api)
  implementation(rootProject.libs.bundles.common)
  implementation(project(":app:service"))
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}
