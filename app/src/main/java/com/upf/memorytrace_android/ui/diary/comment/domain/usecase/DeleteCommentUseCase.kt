package com.upf.memorytrace_android.ui.diary.comment.domain.usecase

import com.upf.memorytrace_android.log.ExceptionLogger
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.ui.diary.comment.domain.CommentRepository
import java.lang.Exception
import javax.inject.Inject

class DeleteCommentUseCase@Inject constructor(
    private val commentRepository: CommentRepository,
    private val exceptionLogger: ExceptionLogger
) {

    suspend operator fun invoke(diaryId: Int, commentId: Int): UiState<List<Comment>> {
        return commentRepository.run {
            try {
                commentRepository.deleteComment(commentId)
                UiState.Success(commentRepository.fetchComments(diaryId))
            } catch (e: Exception) {
                exceptionLogger.logException(e)
                UiState.Failure(e)
            }
        }
    }
}
