package com.upf.memorytrace_android.ui.book.list

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter

internal class BookListAdapter : BaseListAdapter<BookItem>() {
    override fun getItemViewTypeByItemType(item: BookItem) = R.layout.item_book_list
}