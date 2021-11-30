package com.upf.memorytrace_android.ui.diary.comment.domain

data class Comment(
    val commentId: Int,
    val userId: Int,
    val nickname: String,
    val content: String,
    val createdDate: String,
    val isDelete: Int,
    val isReply: Boolean
)