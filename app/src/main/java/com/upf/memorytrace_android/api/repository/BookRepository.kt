package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.BaseResponse
import com.upf.memorytrace_android.api.BookResponse
import com.upf.memorytrace_android.api.util.MemoryTraceUtils

object BookRepository {

    private val PAGE_SIZE = 20

    suspend fun createBook(title: String, bgColor: Int, image: String?) {
        val response = MemoryTraceUtils.apiService().createBook(title, bgColor)
    }

    //TODO: error 객체 관리
    suspend fun fetchBookList(page: Int): BookResponse {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBooks(page, PAGE_SIZE)
            response.let {
//                if (it.isSuccess)
                it
//                else
//                    throw Exception("internal error")
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Internet Error")
        }
    }

    suspend fun fetchBookSetting(id: Int): BaseResponse {

        return try {
            return MemoryTraceUtils.apiService().fetchBookSetting(id)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Internet Error")
        }
    }

    suspend fun exitBook(id: Int): BaseResponse {

        return try {
            return MemoryTraceUtils.apiService().exitBook(id)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Internet Error")
        }
    }
}