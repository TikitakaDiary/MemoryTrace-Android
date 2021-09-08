package com.upf.memorytrace_android.ui.diary.write.sticker

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter


internal class StickerAdapter : BaseListAdapter<StickerItem>() {
    override fun getItemViewTypeByItemType(item: StickerItem) = R.layout.item_sticker
}