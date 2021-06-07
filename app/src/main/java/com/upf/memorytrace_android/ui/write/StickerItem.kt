package com.upf.memorytrace_android.ui.write

import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.base.BaseItem

internal data class StickerItem(
    @DrawableRes val resId: Int,
    override val itemId: String = resId.toString()
) : BaseItem