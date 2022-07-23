package com.upf.memorytrace_android.ui.diary.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.ApiResponse
import com.upf.memorytrace_android.api.map
import com.upf.memorytrace_android.api.toApiResponse
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.data.remote.toEntity
import com.upf.memorytrace_android.ui.diary.data.remote.toEntry
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList
import com.upf.memorytrace_android.ui.diary.list.domain.PinchInfo
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRepository {

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

    override suspend fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        image: File
    ): ApiResponse<Int> {
        return withContext(Dispatchers.IO) {
            val imagePart = MultipartBody.Part.createFormData(
                "img",
                image.name,
                image.asRequestBody("image/*".toMediaType())
            )
            diaryService.postDiary(bookId, title, content, imagePart).toApiResponse().map { it.diaryId }
        }
    }
}