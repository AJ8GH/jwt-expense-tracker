rootProject.name = "jwt-expense-tracker"
include("app")
include("app:api")
findProject(":app:api")?.name = "api"
include("app:persistence")
findProject(":app:persistence")?.name = "persistence"
include("app:service")
findProject(":app:service")?.name = "service"
include("app:component-test")
findProject(":app:component-test")?.name = "component-test"
