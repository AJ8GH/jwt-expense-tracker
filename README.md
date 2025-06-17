# JWT Expense Tracker

[![build](https://github.com/AJ8GH/jwt-expense-tracker/actions/workflows/build.yaml/badge.svg)](https://github.com/AJ8GH/jwt-expense-tracker/actions/workflows/build.yaml)
[![codecov](https://codecov.io/gh/AJ8GH/jwt-expense-tracker/graph/badge.svg?token=2Q4I42S62N)](https://codecov.io/gh/AJ8GH/jwt-expense-tracker)

Expenses tracking app written with Kotlin and Spring Boot. 

Secured with JWT and Spring Security.

Persists expenses using PostgreSQL database.

## Set up and run

### Clone the repo

```sh
git clone git@github.com:AJ8GH/jwt-expense-tracker.git \
&& cd jwt-expense-tracker
```

### Local

```sh
./gradle bootRun
```

### Docker

```sh
docker compose up
```

### Running Tests

```sh
./gradlew test
```

## Dependencies

> Library and plugin versions defined in `gradle/libs.versions.toml`

- Kotlin `2.1.21`
- JDK `21`
- Gradle `8.14.1`
- Spring Boot `3.5.0`
  - spring-boot-starter-web
  - spring-boot-starter-actuator
  - spring-boot-starter-test
- kotlin-logging-jvm
- okhttp
- mockk
- kotest-runner-junit5
- kotest-extensions-spring
- kotest-assertions-core
- kotest-framework-datatest
