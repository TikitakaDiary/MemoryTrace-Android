package com.upf.memorytrace_android.ui.diary.comment.data

import com.upf.memorytrace_android.ui.diary.comment.data.remote.CommentService
import com.upf.memorytrace_android.ui.diary.comment.data.remote.PostCommentRequest
import com.upf.memorytrace_android.ui.diary.comment.data.remote.toEntity
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.ui.diary.comment.domain.CommentRepository
import com.upf.memorytrace_android.util.MemoryTraceConfig
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
): CommentRepository {

    override suspend fun fetchComments(diaryId: Int): List<Comment> {
        return commentService.fetCommentList(diaryId).getOrThrow().flatMap {
            mutableListOf<Comment>().apply {
                val userId = MemoryTraceConfig.uid
                add(it.toEntity(userId == it.userId))
                addAll(it.replies.map { reply -> reply.toEntity(userId == reply.userId)})
            }
        }
    }

    override suspend fun postComment(parentCommentId: Int?, diaryId: Int, content: String) {
        commentService.postComment(PostCommentRequest(parentCommentId, diaryId, content))
    }

    override suspend fun deleteComment(commentId: Int) {
        commentService.deleteComment(commentId)
    }
}