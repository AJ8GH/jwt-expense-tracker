package io.github.aj8gh.expenses.config

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@TestConfiguration
class TestClientConfig {

  @Bean
  @Profile("test")
  fun testOkHttpClient() = OkHttpClient.Builder()
    .addInterceptor(DefaultContentTypeInterceptor())
    .build()
}

class DefaultContentTypeInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    return chain.proceed(
      chain.request()
        .newBuilder()
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .build()
    )
  }
}
