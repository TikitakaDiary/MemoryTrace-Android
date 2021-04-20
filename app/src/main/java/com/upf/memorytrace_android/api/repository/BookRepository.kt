package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.BaseResponse
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.model.BookModel
import com.upf.memorytrace_android.api.model.CreateBookModel
import com.upf.memorytrace_android.api.util.NetworkState

object BookRepository {
    suspend fun createBook(createBookModel: CreateBookModel): NetworkState<BaseResponse> {
        return try {
            val response = MemoryTraceUtils.apiService().createBook(createBookModel)
            response.let {
                NetworkState.Success(it)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun fetchBookList(uid: Int): NetworkState<List<BookModel>> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBooks(uid)
            response.let {
                NetworkState.Success(it)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }
}