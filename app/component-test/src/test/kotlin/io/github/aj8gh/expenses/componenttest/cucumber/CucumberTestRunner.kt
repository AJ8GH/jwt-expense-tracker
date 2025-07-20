package io.github.aj8gh.expenses.componenttest.cucumber

import io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME
import io.cucumber.spring.CucumberContextConfiguration
import io.github.aj8gh.expenses.JwtExpenseTrackerApp
import io.github.aj8gh.expenses.componenttest.config.TestDatabaseConfig
import io.github.aj8gh.expenses.componenttest.config.TestRestClientConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles

private const val TEST_PROFILE = "test"
private const val CUCUMBER_ENGINE = "cucumber"
private const val FEATURES_RESOURCE = "features"
private const val PRETTY_PLUGIN = "pretty"
private const val USAGE_PLUGIN = "usage"

@Suite
@ActiveProfiles(TEST_PROFILE)
@CucumberContextConfiguration
@IncludeEngines(CUCUMBER_ENGINE)
@SelectClasspathResource(FEATURES_RESOURCE)
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = PRETTY_PLUGIN)
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = USAGE_PLUGIN)
@AutoConfigureEmbeddedDatabase(type = POSTGRES, provider = ZONKY)
@SpringBootTest(
  webEnvironment = RANDOM_PORT,
  classes = [
    JwtExpenseTrackerApp::class,
    TestDatabaseConfig::class,
    TestRestClientConfig::class,
  ],
)
class CucumberTestRunner
