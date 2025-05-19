package com.babacan.defactocase.di

import com.babacan.defactocase.data.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        requestBuilder.url(request.url.newBuilder()
            .addQueryParameter(ApiConstants.API_KEY, ApiConstants.API_KEY_VALUE)
            .build())

        return chain.proceed(requestBuilder.build())
    }
}