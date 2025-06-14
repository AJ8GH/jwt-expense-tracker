plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

group = properties["project.group.id"]!!
version = properties["project.version"]!!

dependencies {
  implementation(libs.bundles.implementation)
  runtimeOnly(libs.bundles.runtimeOnly)
  testImplementation(libs.bundles.test.implementation)
}

kotlin {
  jvmToolchain(libs.versions.java.get().toInt())
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(
    tasks.koverHtmlReport,
    tasks.koverXmlReport,
  )
}

kover {
  reports {
    filters {
      excludes {
        classes(
          properties["project.main-class"].toString()
        )
      }
    }
  }
}
