package com.upf.memorytrace_android.ui.diary.list.domain

import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.onSuccess
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import javax.inject.Inject

class PinchUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val fetchPinchInfoUseCase: FetchPinchInfoUseCase
) {

    suspend operator fun invoke(bookId: Int): NetworkState<PinchInfo> {
        val result = diaryRepository.pinch(bookId)
        result.onSuccess {
            return fetchPinchInfoUseCase.invoke(bookId)
        }
        return NetworkState.Failure(result.errorMessageOrNull() ?: "")
    }
}