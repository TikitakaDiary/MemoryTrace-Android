package com.upf.memorytrace_android.ui.diary.detail.domain

data class DiaryDetail(
    val isModifiable: Boolean,
    val diaryId: Int,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrl: String,
    val date: String,
    val isMine: Boolean,
    val commentCount: Int
)