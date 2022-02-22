package com.upf.memorytrace_android.ui.book.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import com.upf.memorytrace_android.ui.book.domain.Book
import com.upf.memorytrace_android.ui.book.domain.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookService: BookService
): BookRepository {

    override suspend fun leaveBook(bookId: Int): NetworkState<Unit> {
        return try {
            bookService.leaveBook(bookId)
            NetworkState.Success(Unit)
        } catch (e: StatusError) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure("")
        }
    }

    override suspend fun fetchBook(bookId: Int): NetworkState<Book> {
        return try {
            NetworkState.Success(bookService.fetchBook(bookId).data!!.toEntity())
        } catch (e: StatusError) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure("")
        }
    }
}