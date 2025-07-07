plugins {
  alias(libs.plugins.kotlin.jpa)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  testImplementation(project(":app:api"))
  testImplementation(project(":app:service"))
  testImplementation(project(":app:persistence"))
  testImplementation(rootProject.libs.bundles.common)
  testImplementation(rootProject.libs.bundles.componentTest)
  testRuntimeOnly(rootProject.libs.bundles.runtimeOnly)
}


tasks.test {
  useJUnitPlatform()
}
