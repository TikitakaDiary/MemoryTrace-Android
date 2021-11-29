package com.upf.memorytrace_android.ui.diary.detail.domain

data class DiaryDetail(
    val isModifiable: Boolean,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrl: String,
    val date: String,
    val commentCount: Int
)