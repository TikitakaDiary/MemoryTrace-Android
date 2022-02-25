package com.upf.memorytrace_android.ui.book.setting.domain

import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.book.domain.BookRepository
import javax.inject.Inject

class LeaveBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(bookId: Int): NetworkState<Unit> {
        return bookRepository.leaveBook(bookId)
    }
}
