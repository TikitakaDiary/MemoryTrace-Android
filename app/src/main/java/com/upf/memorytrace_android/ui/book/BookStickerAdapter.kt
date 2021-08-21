package com.upf.memorytrace_android.ui.book

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseListAdapter
import com.upf.memorytrace_android.ui.write.StickerItem


internal class BookStickerAdapter : BaseListAdapter<StickerItem>() {
    override fun getItemViewTypeByItemType(item: StickerItem) = R.layout.item_book_sticker
}