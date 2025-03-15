package com.superterminais.rivermobile.core

import com.superterminais.rivermobile.data.user.UserSession
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createHttpClientEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine): HttpClient {
    val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 20_000L
            requestTimeoutMillis = 20_000L
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    httpClient.plugin(HttpSend).intercept { request ->
        val ignoreInterceptor = request.headers["Ignore-Interceptor"] != null
        if (ignoreInterceptor) {
            return@intercept execute(request)
        }

        UserSession.getToken()?.let {
            request.headers.append("Authorization", it)
        }

        execute(request)
    }

    return httpClient
}