package com.upf.memorytrace_android.ui.diary.list

import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.R

enum class DiaryListType(@DrawableRes val iconRes: Int) {
    GRID(R.drawable.ic_frame),
    LINEAR(R.drawable.ic_grid);

    fun change(): DiaryListType = when(this) {
        GRID -> LINEAR
        LINEAR -> GRID
    }
}