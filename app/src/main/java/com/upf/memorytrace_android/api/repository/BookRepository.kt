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

    private val PAGE_SIZE = 20
    }

    //TODO: error 객체 관리
    suspend fun fetchBookList(page: Int): BookResponse {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBooks(page, PAGE_SIZE)
            response.let {
                if (it.isSuccess)
                    it
                else
                    throw Exception("internal error")
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Internet Error")
        }
    }

}