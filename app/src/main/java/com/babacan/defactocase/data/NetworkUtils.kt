package com.babacan.defactocase.data

import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

suspend fun <T> apiCall(
    apiCallFun: suspend () -> Response<T>,
): ApiCallResult<T> = try {
    val response = apiCallFun()
    val body = response.body()

    if (response.isSuccessful && body != null) {
        ApiCallResult.Success((body))
    } else {
        ApiCallResult.Error(
            when (response.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorReason.Authorization
                else -> {
                    ErrorReason.Unspecified(
                        httpResponseCode = response.code(),
                        errorMessage = response.message(),
                    )
                }
            }
        )
    }
} catch (e: Exception) {
    e.printStackTrace()
    val error = when (e) {
        is SocketTimeoutException,
        is UnknownHostException,
        is SSLHandshakeException -> ErrorReason.NetworkConnection
        else -> ErrorReason.Unspecified(errorMessage = e.message)
    }
    ApiCallResult.Error(error)
}
