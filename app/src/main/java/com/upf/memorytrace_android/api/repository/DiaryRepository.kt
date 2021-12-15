package com.upf.memorytrace_android.api.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.model.DiaryListModel
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import okhttp3.MultipartBody

@Deprecated("더이상 static 으로 사용하지 않습니다.", ReplaceWith("com.upf.memorytrace_android.ui.diary.data.DiaryRepository"))
internal object DiaryRepository {
    private const val SUCCESS = "Success"
    private const val ERROR = "예상하지 못한 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."

    private const val size = 100

    suspend fun fetchDiaries(id: Int, page: Int): NetworkState<DiaryListModel> {
        return try {
            val response = MemoryTraceUtils.apiService().fetchDiaries(id, page, size)
            NetworkState.Success(response.data as DiaryListModel)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun createDiary(
        bid: Int,
        title: String,
        content: String,
        img: MultipartBody.Part
    ): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().createDiary(bid, title, content, img)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun modifyDiary(
        did: Int,
        title: String,
        content: String,
        img: MultipartBody.Part
    ): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().modifyDiary(did, title, content, img)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }
}