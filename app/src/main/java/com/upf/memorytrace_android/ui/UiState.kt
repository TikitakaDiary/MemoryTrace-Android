package com.upf.memorytrace_android.ui

sealed class UiState<out T> {
    data class Success<T>(val data: T): UiState<T>()
    object Loading: UiState<Nothing>()
    data class Failure(val throwable: Throwable?): UiState<Nothing>()
}
