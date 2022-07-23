package com.upf.memorytrace_android.ui.diary.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.data.remote.toEntity
import com.upf.memorytrace_android.ui.diary.data.remote.toEntry
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepositoryOld
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList
import com.upf.memorytrace_android.ui.diary.list.domain.PinchInfo
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Deprecated(
    "새로운 Retrofit 으로 주입받기 위해 해당 클래스는 deprecated 합니다.\n" +
            "DiaryRepository 로 남은 함수를 이동시켜주세요.",
    ReplaceWith("DiaryRepository", "com.upf.memorytrace_android.data.network")
)
class DiaryRepositoryOldImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRepositoryOld {

    override suspend fun fetchDiary(diaryId: Int): DiaryDetail {
        return withContext(Dispatchers.IO) {
            diaryService.fetchDiary(diaryId).data!!.toEntry(MemoryTraceConfig.uid)!!
        }
    }

    override suspend fun fetchDiaries(bookId: Int, page: Int, size: Int): NetworkState<DiaryList> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkState.Success(
                    MemoryTraceUtils.apiService().fetchDiaries(bookId, page, size).data!!.toEntity()
                )
            } catch (e: StatusError) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.responseMessage)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.message ?: "")
            }
        }
    }

    override suspend fun fetchPinchInfo(bookId: Int): NetworkState<PinchInfo> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkState.Success(
                    diaryService.fetchPinchInfo(bookId).data!!.toEntity()
                )
            } catch (e: StatusError) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.responseMessage)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.message ?: "")
            }
        }
    }

    override suspend fun pinch(bookId: Int): NetworkState<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                diaryService.pinch(bookId)
                NetworkState.Success(Unit)
            } catch (e: StatusError) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.responseMessage)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                NetworkState.Failure(e.message ?: "")
            }
        }
    }
}