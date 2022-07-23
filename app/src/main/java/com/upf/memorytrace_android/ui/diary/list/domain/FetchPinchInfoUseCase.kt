package com.upf.memorytrace_android.ui.diary.list.domain

import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepositoryOld
import javax.inject.Inject

class FetchPinchInfoUseCase @Inject constructor(
    private val diaryRepository: DiaryRepositoryOld
) {

    suspend operator fun invoke(bookId: Int): NetworkState<PinchInfo> {
        return diaryRepository.fetchPinchInfo(bookId)
    }
}