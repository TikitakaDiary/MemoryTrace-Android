package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.util.MemoryTraceUtils

internal object DiaryRepository {
    private const val size = 5

    suspend fun fetchDiaries(id: Int) = MemoryTraceUtils.apiService().fetchDiaries(id, 1, size)
}