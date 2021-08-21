package com.upf.memorytrace_android.ui.book

import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.base.BaseItem

internal data class BookItem(
    val book: Book,
    override val itemId: String = book.bid.toString()
) : BaseItem