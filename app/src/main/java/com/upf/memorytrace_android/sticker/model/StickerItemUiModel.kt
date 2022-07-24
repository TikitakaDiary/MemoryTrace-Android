package com.upf.memorytrace_android.sticker.model

import androidx.annotation.DrawableRes

data class StickerItemUiModel(
    @DrawableRes val imageResource: Int,
    val onClick: () -> Unit,
)
