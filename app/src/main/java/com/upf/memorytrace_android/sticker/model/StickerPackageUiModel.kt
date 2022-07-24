package com.upf.memorytrace_android.sticker.model

import androidx.annotation.DrawableRes

data class StickerPackageUiModel(
    @DrawableRes val imageResource: Int,
    val stickers: List<StickerItemUiModel> = emptyList(),
)
