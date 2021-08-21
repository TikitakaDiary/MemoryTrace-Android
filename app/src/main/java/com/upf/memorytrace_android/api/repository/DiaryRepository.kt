package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import okhttp3.MultipartBody

internal object DiaryRepository {
    private const val size = 100

    suspend fun fetchDiaries(id: Int, page: Int) = MemoryTraceUtils.apiService().fetchDiaries(id, page, size)
    suspend fun fetchDiary(id: Int) = MemoryTraceUtils.apiService().fetchDiary(id)
    suspend fun createDiary(bid: Int, title: String, content: String, img: MultipartBody.Part) =
        MemoryTraceUtils.apiService().createDiary(bid, title, content, img)
}