package com.upf.memorytrace_android.ui.diary.comment.domain

import com.upf.memorytrace_android.ui.UiState
import java.lang.Exception
import javax.inject.Inject

class DeleteCommentUseCase@Inject constructor(
    private val commentRepository: CommentRepository,
) {

    suspend operator fun invoke(diaryId: Int, commentId: Int): UiState<List<Comment>> {
        return commentRepository.run {
            try {
                commentRepository.deleteComment(commentId)
                UiState.Success(commentRepository.fetchComments(diaryId))
            } catch (e: Exception) {
                UiState.Failure(e)
            }
        }
    }
}
