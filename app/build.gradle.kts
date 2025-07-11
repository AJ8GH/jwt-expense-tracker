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
  kover(project(":app:component-test"))
  kover(project(":app:api"))
  kover(project(":app:persistence"))
  kover(project(":app:service"))
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

tasks.test {
  useJUnitPlatform()
  finalizedBy(
    tasks.koverHtmlReport,
    tasks.koverXmlReport,
  )
}

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
  }

  group = properties["project.group.id"]!!
  version = properties["project.version"]!!
}

tasks.bootJar {
  mainClass = properties["project.main-class"].toString()
}
