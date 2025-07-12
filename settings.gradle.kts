rootProject.name = prop("project.name")

val rootModule = prop("module.root")

listOf(
  "root",
  "persistence",
  "presentation",
  "business",
  "component-test"
).forEach {
  prop("module.$it")
    .let { module ->
      if (module == rootModule) {
        include(module)
      } else {
        include("$rootModule:$module")
        findProject(module)?.name = ":$rootModule:$module"
      }
    }
}

private fun prop(key: String) = extra[key]
  .toString()
  .also { println("got value $it for key $key") }
