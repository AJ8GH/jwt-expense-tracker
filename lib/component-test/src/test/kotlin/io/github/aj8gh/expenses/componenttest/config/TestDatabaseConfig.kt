package io.github.aj8gh.expenses.componenttest.config

import io.zonky.test.db.config.DatabaseProviderFactory
import io.zonky.test.db.config.DatabaseProviderFactory.PipelineBuilder
import io.zonky.test.db.provider.DatabaseProvider
import org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Role

@TestConfiguration
class TestDatabaseConfig {

  @Bean
  @Primary
  @Role(ROLE_INFRASTRUCTURE)
  @ConditionalOnMissingBean(name = ["postgresDatabaseProviderFactory"])
  fun postgresDatabaseProviderFactory(
    defaultDatabaseProviderFactory: DatabaseProviderFactory,
  ): DatabaseProviderFactory = defaultDatabaseProviderFactory
    .customizeProvider { builder: PipelineBuilder, provider: DatabaseProvider? ->
      builder.optimizingProvider(
        builder.prefetchingProvider(builder.templatingProvider(provider))
      )
    }
}
