package com.upf.memorytrace_android.ui.book.domain

import com.upf.memorytrace_android.api.util.NetworkState


interface BookRepository {

    suspend fun leaveBook(bookId: Int): NetworkState<Unit>
}