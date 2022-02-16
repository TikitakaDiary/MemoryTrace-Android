package com.upf.memorytrace_android

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure(val errorMessage: String) : ApiResult<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun errorMessageOrNull(): String? = when (this) {
        is Failure -> errorMessage
        else -> null
    }
}

inline fun <T> ApiResult<T>.onSuccess(block: (data: T) -> Unit): ApiResult<T> = also {
    getOrNull()?.let(block)
}

inline fun <T> ApiResult<T>.onFailure(block: (errorMessage: String) -> Unit): ApiResult<T> = also {
    errorMessageOrNull()?.let(block)
}