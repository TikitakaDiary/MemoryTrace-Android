package com.upf.memorytrace_android.api.util

sealed class NetworkState<out T> {
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Failure(val message: String) : NetworkState<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun errorMessageOrNull(): String? = when (this) {
        is Failure -> message
        else -> null
    }
}

inline fun <T> NetworkState<T>.onSuccess(
    block: (data: T) -> Unit
): NetworkState<T> = also {
    getOrNull()?.let(block)
}

inline fun <T> NetworkState<T>.onFailure(
    block: (errorMessage: String) -> Unit
): NetworkState<T> = also {
    errorMessageOrNull()?.let(block)
}