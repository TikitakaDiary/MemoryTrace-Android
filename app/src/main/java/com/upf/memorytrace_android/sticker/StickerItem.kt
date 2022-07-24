package com.upf.memorytrace_android.sticker

import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.ui.base.BaseItem

@Deprecated("SelectStickerDialogFragment 를 사용하세요.")
internal data class StickerItem(
    @DrawableRes val resId: Int,
    override val itemId: String = resId.toString()
) : BaseItem