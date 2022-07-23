package com.upf.memorytrace_android.ui.diary.write

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
class DiaryDetailDTO(
    val diaryId: Int,
    val title: String,
    val content: String,
    val date: String,
    val writerName: String,
    val imageUrl: String
): Parcelable
