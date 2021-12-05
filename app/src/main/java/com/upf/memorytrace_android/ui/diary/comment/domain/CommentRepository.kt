package com.upf.memorytrace_android.ui.diary.comment.domain

interface CommentRepository {
    suspend fun fetchComments(diaryId: Int): List<Comment>
    suspend fun postComment(parentCommentId: Int?, diaryId: Int, content: String)
}
