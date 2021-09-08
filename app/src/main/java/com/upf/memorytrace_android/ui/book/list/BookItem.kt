package com.upf.memorytrace_android.ui.book.list

import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.ui.base.BaseItem

internal data class BookItem(
    val book: Book,
    override val itemId: String = book.bid.toString()
) : BaseItem