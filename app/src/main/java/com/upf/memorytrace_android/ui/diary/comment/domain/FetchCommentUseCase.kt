package com.upf.memorytrace_android.ui.diary.comment.domain

import com.upf.memorytrace_android.ui.UiState
import javax.inject.Inject

class FetchCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {

    suspend operator fun invoke(diaryId: Int): UiState<List<Comment>> {
        return try {
            UiState.Success(commentRepository.fetchComments(diaryId))
        } catch (e: Exception) {
            UiState.Failure(e)
        }
    }
}