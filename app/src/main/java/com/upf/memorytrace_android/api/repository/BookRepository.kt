package com.upf.memorytrace_android.api.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.model.BookList
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import okhttp3.MultipartBody

@Deprecated("더이상 static 으로 사용하지 않습니다.", ReplaceWith("com.upf.memorytrace_android.ui.book.domain.DiaryRepository"))
object BookRepository {

    const val PAGE_SIZE = 50
    private const val SUCCESS = "Success"
    private const val ERROR = "예상하지 못한 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."

    suspend fun createBook(
        title: String,
        bgColor: Int,
        image: MultipartBody.Part? = null
    ): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().createBook(title, bgColor, image)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun updateBook(
        bid: Int,
        title: String,
        bgColor: Int,
        image: MultipartBody.Part? = null
    ): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().updateBook(bid, title, bgColor, image)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun fetchBookList(page: Int): NetworkState<BookList> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBooks(page, PAGE_SIZE)
            NetworkState.Success(response.data as BookList)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    @Deprecated("더이상 사용하지 않습니다.", ReplaceWith("com.upf.memorytrace_android.ui.book.domain.BookRepository#fetchBook()"))
    suspend fun fetchBook(bid: Int): NetworkState<Book> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchBook(bid)
            NetworkState.Success(response.data as Book)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }
}