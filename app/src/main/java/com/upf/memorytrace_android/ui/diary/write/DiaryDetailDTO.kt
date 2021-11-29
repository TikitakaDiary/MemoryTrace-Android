package com.upf.memorytrace_android.ui.diary.write

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiaryDetailDTO(
    val diaryId: Int,
    val title: String,
    val content: String,
    val imageUrl: String
): Parcelable
