package com.upf.memorytrace_android.ui.diary.write.sticker

import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.ui.base.BaseItem

internal data class StickerItem(
    @DrawableRes val resId: Int,
    override val itemId: String = resId.toString()
) : BaseItem