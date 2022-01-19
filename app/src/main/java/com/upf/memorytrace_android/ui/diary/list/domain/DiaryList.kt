package com.upf.memorytrace_android.ui.diary.list.domain

data class DiaryList(
    val curPage: Int,
    val hasNext: Boolean,
    val title: String,
    val whoseTurn: Int,
    val diaryList: List<Diary>
)