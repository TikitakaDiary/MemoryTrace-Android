package com.upf.memorytrace_android.ui.diary.comment.domain

import com.upf.memorytrace_android.ui.UiState
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {

    suspend operator fun invoke(
        parentCommentId: Int?,
        diaryId: Int,
        content: String
    ): UiState<List<Comment>> {
        return commentRepository.run {
            try {
                postComment(parentCommentId, diaryId, content)
                UiState.Success(commentRepository.fetchComments(diaryId))
            } catch (e: Exception) {
                UiState.Failure(e)
            }
        }
    }
}