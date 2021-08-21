package com.upf.memorytrace_android.ui.book

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseListAdapter

internal class BookListAdapter : BaseListAdapter<BookItem>() {
    override fun getItemViewTypeByItemType(item: BookItem) = R.layout.item_book_list
}