package com.upf.memorytrace_android.ui

sealed class UiState {
    data class Success<T>(val data: T): UiState()
    object Loading: UiState()
    data class Failure(val throwable: Throwable?): UiState()
}
