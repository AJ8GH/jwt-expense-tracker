import io.github.aj8gh.expenses.plugin.BUSINESS_MODULE
import io.github.aj8gh.expenses.plugin.COMPONENT_TEST_MODULE
import io.github.aj8gh.expenses.plugin.PERSISTENCE_MODULE
import io.github.aj8gh.expenses.plugin.PRESENTATION_MODULE
import io.github.aj8gh.expenses.plugin.moduleId
import io.github.aj8gh.expenses.plugin.prop

plugins {
  alias(libs.plugins.kotlin.jpa)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kover)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

kotlin {
  jvmToolchain(rootProject.libs.versions.java.get().toInt())
}

dependencies {
  kover(project(moduleId(COMPONENT_TEST_MODULE)))
  kover(project(moduleId(BUSINESS_MODULE)))
  kover(project(moduleId(PERSISTENCE_MODULE)))
  kover(project(moduleId(PRESENTATION_MODULE)))
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(
    tasks.koverHtmlReport,
    tasks.koverXmlReport,
  )
}

val mainClassValue = prop("project.main-class")

kover {
  reports {
    filters {
      excludes {
        classes(mainClassValue)
      }
    }
  }
}

tasks.bootJar {
  mainClass = mainClassValue
}

extra["junit-jupiter.version"] = libs.versions.junit.jupiter.get()

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
  }

  group = prop("project.group.id")
  version = prop("project.version")
}
