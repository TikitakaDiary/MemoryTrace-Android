package com.upf.memorytrace_android.ui.diary.comment.data

import com.upf.memorytrace_android.ui.diary.comment.data.remote.CommentService
import com.upf.memorytrace_android.ui.diary.comment.data.remote.PostCommentRequest
import com.upf.memorytrace_android.ui.diary.comment.data.remote.ReplyResponse
import com.upf.memorytrace_android.ui.diary.comment.data.remote.toEntity
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.ui.diary.comment.domain.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
): CommentRepository {

    override suspend fun fetchComments(diaryId: Int): List<Comment> {
        return commentService.fetCommentList(diaryId).getOrThrow().flatMap {
            mutableListOf<Comment>().apply {
                add(it.toEntity())
                addAll(it.replies.map(ReplyResponse::toEntity))
            }
        }
    }

    override suspend fun postComment(parentCommentId: Int?, diaryId: Int, content: String) {
        commentService.postComment(PostCommentRequest(parentCommentId, diaryId, content))
    }
}