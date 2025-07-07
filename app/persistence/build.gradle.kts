plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.jpa)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(rootProject.libs.bundles.persistence)
  implementation(rootProject.libs.bundles.common)
  runtimeOnly(rootProject.libs.bundles.runtimeOnly)
}
