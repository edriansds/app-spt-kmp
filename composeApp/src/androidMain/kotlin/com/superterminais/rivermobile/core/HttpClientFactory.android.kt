package com.superterminais.rivermobile.core

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.config
import io.ktor.client.engine.okhttp.OkHttp
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

actual fun createHttpClientEngine(): HttpClientEngine {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
            arrayOf()

        override fun checkClientTrusted(
            chain: Array<java.security.cert.X509Certificate>, authType: String
        ) {
        }

        override fun checkServerTrusted(
            chain: Array<java.security.cert.X509Certificate>, authType: String
        ) {
        }
    })

    val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    return OkHttp.config {
        this.config {
            sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }
        }
    }.create()
}