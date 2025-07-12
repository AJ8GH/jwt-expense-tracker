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
  kover(project(moduleId("component-test")))
  kover(project(moduleId("business")))
  kover(project(moduleId("persistence")))
  kover(project(moduleId("presentation")))
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

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
  }

  group = prop("project.group.id")
  version = prop("project.version")
}
