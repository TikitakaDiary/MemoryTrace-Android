package com.upf.memorytrace_android.ui.diary.detail.domain

import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import javax.inject.Inject

class FetchDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(diaryId: Int): UiState {
        return try {
            UiState.Success(diaryRepository.fetchDiary(diaryId))
        } catch (e: Exception) {
            UiState.Failure(e)
        }
    }
}