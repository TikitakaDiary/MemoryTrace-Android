package com.upf.memorytrace_android.ui.diary.domain

import com.upf.memorytrace_android.api.ApiResponse
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList
import com.upf.memorytrace_android.ui.diary.list.domain.PinchInfo
import java.io.File

interface DiaryRepository {
    suspend fun fetchDiary(diaryId: Int): DiaryDetail

    suspend fun fetchDiaries(bookId: Int, page: Int, size: Int): NetworkState<DiaryList>

    suspend fun fetchPinchInfo(bookId: Int): NetworkState<PinchInfo>

    suspend fun pinch(bookId: Int): NetworkState<Unit>

    suspend fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        image: File
    ): ApiResponse<Int>

    suspend fun editDiary(
        diaryId: Int,
        title: String,
        content: String,
        image: File
    ): ApiResponse<Int>
}