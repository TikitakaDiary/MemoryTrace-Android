package com.upf.memorytrace_android.ui.diary

import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.R

internal enum class DiaryListType(@DrawableRes val iconRes: Int) {
    GRID(R.drawable.ic_frame),
    FRAME(R.drawable.ic_grid)
}