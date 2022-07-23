package com.upf.memorytrace_android.ui.diary.write.color

import com.upf.memorytrace_android.color.UserColor

data class ColorItemUiModel(
    val color: UserColor = UserColor.NONE,
    val isSelected: Boolean = false,
    val onSelectColorItem: () -> Unit
)