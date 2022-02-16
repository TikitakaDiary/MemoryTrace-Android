package com.upf.memorytrace_android.ui.diary.list.domain

import java.util.*

data class Diary(
    val diaryId: Int,
    val title: String,
    val nickname: String,
    val imageUrl: String,
    val createdDate: Date
)