package com.upf.memorytrace_android.ui.diary.list.domain

import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import javax.inject.Inject

class FetchDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(bookId: Int, page: Int, size: Int): NetworkState<DiaryList> {
        return diaryRepository.fetchDiaries(bookId, page, size)
    }
}