package com.upf.memorytrace_android.ui.write

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseListAdapter


internal class StickerAdapter : BaseListAdapter<StickerItem>() {
    override fun getItemViewTypeByItemType(item: StickerItem) = R.layout.item_sticker
}