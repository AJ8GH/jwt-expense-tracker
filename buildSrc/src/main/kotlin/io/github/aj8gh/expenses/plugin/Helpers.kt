package io.github.aj8gh.expenses.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

fun Project.prop(key: String) = extra[key].toString()
  .also { println("got value $it for key $key") }

fun Project.moduleId(module: String) = "" +
    ":${prop("module.root")}:${prop("module.$module")}"
