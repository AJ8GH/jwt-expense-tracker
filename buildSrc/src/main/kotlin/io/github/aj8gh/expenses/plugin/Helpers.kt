package io.github.aj8gh.expenses.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

const val PREFIX = "module."
const val SEPARATOR = ":"
const val ROOT_MODULE = "root"
const val APP_MODULE = "app"
const val BUSINESS_MODULE = "business"
const val PERSISTENCE_MODULE = "persistence"
const val PRESENTATION_MODULE = "presentation"
const val COMPONENT_TEST_MODULE = "component-test"

fun Project.prop(key: String) = extra[key].toString()
  .also { println("got value $it for key $key") }

fun Project.moduleId(module: String) =
  "$SEPARATOR${prop("$PREFIX$ROOT_MODULE")}$SEPARATOR${prop("$PREFIX$module")}"
