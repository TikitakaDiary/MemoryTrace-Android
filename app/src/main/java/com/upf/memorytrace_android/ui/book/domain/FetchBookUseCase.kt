package com.upf.memorytrace_android.ui.book.domain

import com.upf.memorytrace_android.api.util.NetworkState
import javax.inject.Inject

class FetchBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: Int): NetworkState<Book> {
        return bookRepository.fetchBook(bookId)
    }
}