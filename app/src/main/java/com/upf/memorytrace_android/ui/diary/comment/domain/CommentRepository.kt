package com.upf.memorytrace_android.ui.diary.comment.domain

interface CommentRepository {
    suspend fun fetchComments(diaryId: Int): List<Comment>
}
