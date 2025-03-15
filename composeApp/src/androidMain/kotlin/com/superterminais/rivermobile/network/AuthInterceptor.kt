package com.superterminais.rivermobile.network

import com.superterminais.rivermobile.data.user.UserSession
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val response = addTokenToRequest(chain)

        return when (response.code) {
            401 -> response.newBuilder().message("Credenciais inválidas ou expiradas.").code(401)
                .build()

            else -> response
        }
    }

    private fun addTokenToRequest(chain: Chain): Response {
        if (chain.request()
                .header("Ignore-Interceptor") != null
        ) return chain.proceed(chain.request())

        if (UserSession.getToken() == null) return chain.proceed(chain.request())

        val requestBuilder = chain.request().newBuilder()
        UserSession.getToken()?.let {
            requestBuilder.addHeader("Authorization", it)
        }
        return chain.proceed(requestBuilder.build())
    }
}