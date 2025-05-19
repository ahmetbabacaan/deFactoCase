package com.babacan.defactocase.data

import java.net.HttpURLConnection


/**
 * wrapper for network call results that makes it easy to handle success and error cases.
 */
sealed interface ApiCallResult<out T> {

    data class Success<out T>(val data: T) : ApiCallResult<T>

    @JvmInline
    value class Error(val reason: ErrorReason) : ApiCallResult<Nothing>
}

sealed class ErrorReason(
    val httpResponseCode: Int?,
    val errorMessage: String? = null,
    val exception: Exception? = null
) {
    data object NetworkConnection : ErrorReason(null, null, null)
    data object Authorization : ErrorReason(HttpURLConnection.HTTP_UNAUTHORIZED, null, null)
    class Unspecified(
        httpResponseCode: Int? = null,
        errorMessage: String? = null,
        exception: Exception? = null
    ) : ErrorReason(
        httpResponseCode = httpResponseCode,
        errorMessage = errorMessage,
        exception = exception
    )
}

fun <T> ApiCallResult<T>.getOrNull(): T? {
    return if (this is ApiCallResult.Success) {
        this.data
    } else {
        null
    }
}


fun <T> ApiCallResult<T>.mapToResult(): Result<T> {
    return when (this) {
        is ApiCallResult.Success -> Result.success(this.data)
        is ApiCallResult.Error -> {
            Result.failure(
                this.reason.exception ?: Exception(this.reason.errorMessage)
            )
        }
    }
}
