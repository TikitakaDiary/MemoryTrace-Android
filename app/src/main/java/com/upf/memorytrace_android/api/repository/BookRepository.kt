package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.model.BookList
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import okhttp3.MultipartBody

object BookRepository {

     val PAGE_SIZE = 50

    suspend fun createBook(
        title: String,
        bgColor: Int,
        image: MultipartBody.Part? = null
    ): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().createBook(title, bgColor, image)
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: java.lang.Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun updateBook(
        bid: Int,
        title: String,
        bgColor: Int,
        image: MultipartBody.Part? = null
    ): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().updateBook(bid, title, bgColor, image)
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: java.lang.Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun fetchBookList(page: Int): NetworkState<BookList?> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBooks(page, PAGE_SIZE)
            if (response.isSuccess)
                NetworkState.Success(response.data)
            else
                NetworkState.Failure(response.responseMessage)
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun fetchBook(bid: Int): NetworkState<Book?> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBook(bid)
            if (response.isSuccess) {
                NetworkState.Success(response.data)
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }


    suspend fun leaveBook(bid: Int): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().leaveBook(bid)
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }
}