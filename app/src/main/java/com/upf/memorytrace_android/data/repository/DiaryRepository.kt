package com.upf.memorytrace_android.data.repository

import com.upf.memorytrace_android.api.ApiResponse
import java.io.File

interface DiaryRepository {

    suspend fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        imageFile: File
    ): ApiResponse<Int>

    suspend fun editDiary(
        diaryId: Int,
        title: String,
        content: String,
        imageFile: File
    ): ApiResponse<Int>
}