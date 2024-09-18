package com.example.foodieapp.data.api.dto

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token") // Thêm Bearer token vào header
            .build()
        return chain.proceed(requestWithToken)
    }
}