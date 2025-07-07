package io.github.aj8gh.expenses.config

import io.github.aj8gh.expenses.constant.AUTH_PATH
import io.github.aj8gh.expenses.constant.ERROR_PATH
import io.github.aj8gh.expenses.constant.PARTIES_PATH
import io.github.aj8gh.expenses.constant.WILDCARD_PATH
import io.github.aj8gh.expenses.service.security.JwtAuthorizationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
  @Value("\${auth.password.strength}") private val passwordStrength: Int,
) {

  @Bean
  fun securityFilterChain(
    http: HttpSecurity,
    jwtAuthenticationFilter: JwtAuthorizationFilter,
    authenticationProvider: AuthenticationProvider,
  ): DefaultSecurityFilterChain = http
    .csrf { it.disable() }
    .authorizeHttpRequests {
      it.requestMatchers(PARTIES_PATH, AUTH_PATH, "$AUTH_PATH$WILDCARD_PATH", ERROR_PATH)
        .permitAll()
        .anyRequest()
        .fullyAuthenticated()
    }
    .sessionManagement { it.sessionCreationPolicy(STATELESS) }
    .authenticationProvider(authenticationProvider)
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    .build()

  @Bean
  fun authenticationProvider(
    userDetailsService: UserDetailsService,
    passwordEncoder: PasswordEncoder,
  ): AuthenticationProvider = DaoAuthenticationProvider(userDetailsService)
    .also { it.setPasswordEncoder(passwordEncoder) }

  @Bean
  fun PasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder(passwordStrength)

  @Bean
  fun authenticationManager(
    authConfig: AuthenticationConfiguration,
  ): AuthenticationManager = authConfig.authenticationManager
}
