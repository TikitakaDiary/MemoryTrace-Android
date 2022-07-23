package com.upf.memorytrace_android.api.util

data class ApiError(
    val requestUrl: String,
    val errorCode: String,
    val errorMessage: String?,
    override val cause: Throwable?
) : Exception("$requestUrl\n[$errorCode] $errorMessage")

fun Throwable.apiErrorMessage(): String? {
    return (this as? ApiError)?.errorMessage
}