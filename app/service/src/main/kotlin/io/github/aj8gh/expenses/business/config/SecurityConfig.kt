package io.github.aj8gh.expenses.business.config

import io.github.aj8gh.expenses.business.constant.AUTH_PATH
import io.github.aj8gh.expenses.business.constant.ERROR_PATH
import io.github.aj8gh.expenses.business.constant.PARTIES_PATH
import io.github.aj8gh.expenses.business.constant.WILDCARD_PATH
import io.github.aj8gh.expenses.business.service.security.JwtAuthorizationFilter
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
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
  @Value("\${server.servlet.context-path}") private val contextPath: String,
) {

  @Bean
  fun securityFilterChain(
    http: HttpSecurity,
    jwtAuthenticationFilter: JwtAuthorizationFilter,
    authenticationProvider: AuthenticationProvider,
  ): DefaultSecurityFilterChain = http
    .csrf { it.disable() }
    .authorizeHttpRequests {
      it.requestMatchers(ERROR_PATH).permitAll()
        .requestMatchers(PARTIES_PATH).permitAll()
        .requestMatchers("$PARTIES_PATH/").permitAll()
        .requestMatchers(AUTH_PATH).permitAll()
        .requestMatchers("$AUTH_PATH$WILDCARD_PATH").permitAll()
        .anyRequest().fullyAuthenticated()
    }
    .sessionManagement { it.sessionCreationPolicy(STATELESS) }
    .authenticationProvider(authenticationProvider)
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    .exceptionHandling {
      it.authenticationEntryPoint { _, response, _ ->
        response.sendError(SC_UNAUTHORIZED)
      }
    }
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
