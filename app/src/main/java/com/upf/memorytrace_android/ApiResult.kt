package com.upf.memorytrace_android

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure(val throwable: Throwable) : ApiResult<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun throwableOrNull(): Throwable? = when (this) {
        is Failure -> throwable
        else -> null
    }
}

inline fun <T> ApiResult<T>.onSuccess(block: (data: T) -> Unit): ApiResult<T> = also {
    getOrNull()?.let(block)
}

inline fun <T> ApiResult<T>.onFailure(block: (throwable: Throwable) -> Unit): ApiResult<T> = also {
    throwableOrNull()?.let(block)
}