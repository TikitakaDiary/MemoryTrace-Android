package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import okhttp3.MultipartBody

internal object DiaryRepository {
    private const val size = 5

    suspend fun fetchDiaries(id: Int) = MemoryTraceUtils.apiService().fetchDiaries(id, 1, size)

    suspend fun createDiary(bid: Int, title: String, content: String, img: MultipartBody.Part) =
        MemoryTraceUtils.apiService().createDiary(bid, title, content, img)
}