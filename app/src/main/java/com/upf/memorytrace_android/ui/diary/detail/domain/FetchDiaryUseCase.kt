package com.upf.memorytrace_android.ui.diary.detail.domain

import com.upf.memorytrace_android.log.ExceptionLogger
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import javax.inject.Inject

class FetchDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val exceptionLogger: ExceptionLogger
) {

    suspend operator fun invoke(diaryId: Int): UiState<DiaryDetail> {
        return try {
            UiState.Success(diaryRepository.fetchDiary(diaryId))
        } catch (e: Exception) {
            exceptionLogger.logException(e)
            UiState.Failure(e)
        }
    }
}