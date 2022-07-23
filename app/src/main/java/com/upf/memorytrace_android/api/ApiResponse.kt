package com.upf.memorytrace_android.api

import com.google.gson.Gson
import com.upf.memorytrace_android.api.model.BaseResponse
import com.upf.memorytrace_android.api.util.ApiError
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure(val throwable: Throwable) : ApiResponse<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun throwableOrNull(): Throwable? = when (this) {
        is Failure -> throwable
        else -> null
    }
}

inline fun <T> ApiResponse<T>.onSuccess(
    block: (data: T) -> Unit
): ApiResponse<T> = also {
    getOrNull()?.let(block)
}

inline fun <T> ApiResponse<T>.onFailure(
    block: (throwable: Throwable) -> Unit
): ApiResponse<T> = also {
    throwableOrNull()?.let(block)
}

suspend fun <T : Any> Call<T>.toApiResponse(): ApiResponse<T> {
    return try {
        val result = await()
        ApiResponse.Success(result)
    } catch (e: HttpException) {
        val requestUrl = request().url.toString()
        val errorBody = e.response()?.errorBody()
            ?: return ApiResponse.Failure(IOException("$requestUrl\nNo errorBody", e))

        val result: BaseResponse<*> =
            Gson().fromJson(errorBody.charStream(), BaseResponse::class.java)
                ?: return ApiResponse.Failure(IOException("$requestUrl\nParse error", e))

        ApiResponse.Failure(
            ApiError(
                requestUrl = requestUrl,
                errorCode = result.statusCode,
                errorMessage = result.responseMessage,
                cause = e
            )
        )
    } catch (e: Exception) {
        ApiResponse.Failure(e)
    }
}

inline fun <R, T> ApiResponse<T>.map(transform: (value: T) -> R): ApiResponse<R> {
    return when (this) {
        is ApiResponse.Success -> ApiResponse.Success(transform(data))
        is ApiResponse.Failure -> ApiResponse.Failure(throwable)
    }
}