// package io.github.aj8gh.expenses.client
//
// import com.fasterxml.jackson.databind.ObjectMapper
// import io.github.oshai.kotlinlogging.KotlinLogging
// import okhttp3.Headers
// import okhttp3.MediaType.Companion.toMediaType
// import okhttp3.OkHttpClient
// import okhttp3.Request
// import okhttp3.RequestBody.Companion.toRequestBody
// import okhttp3.Response
// import org.springframework.http.HttpMethod.GET
// import org.springframework.http.HttpMethod.POST
// import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
//
// private val logger = KotlinLogging.logger { }
//
// class AppClient(
//   private val client: OkHttpClient,
//   private val mapper: ObjectMapper,
//   private val port: Int,
//   private val basePath: String,
// ) {
//
//   fun get(path: String, headers: Headers = Headers.EMPTY) =
//     call(path = path, method = GET.name(), headers = headers)
//
//   fun post(path: String, requestBody: Any, headers: Headers = Headers.EMPTY) =
//     call(path = path, method = POST.name(), headers = headers, body = requestBody)
//
//   private fun call(
//     path: String,
//     method: String,
//     headers: Headers,
//     body: Any? = null,
//   ): Response {
//     val requestBody = body?.let {
//       mapper.writeValueAsString(it)
//         .toRequestBody(APPLICATION_JSON_VALUE.toMediaType())
//     }
//
//     val request = Request.Builder()
//       .url("http://localhost:$port$basePath$path")
//       .method(method, requestBody)
//       .headers(headers)
//       .build()
//
//     return client.newCall(request).execute()
//       .also { logResponse(it) }
//   }
//
//   private fun logResponse(response: Response) = logger.info { "Response status '${response.code}'" }
// }
