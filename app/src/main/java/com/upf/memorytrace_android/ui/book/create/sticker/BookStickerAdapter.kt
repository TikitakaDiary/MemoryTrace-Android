package com.upf.memorytrace_android.ui.book.create.sticker

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter
import com.upf.memorytrace_android.sticker.StickerItem


internal class BookStickerAdapter : BaseListAdapter<StickerItem>() {
    override fun getItemViewTypeByItemType(item: StickerItem) = R.layout.item_book_sticker
}