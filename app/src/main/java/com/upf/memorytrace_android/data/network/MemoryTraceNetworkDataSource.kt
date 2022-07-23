package com.upf.memorytrace_android.data.network

import com.upf.memorytrace_android.data.network.response.PostDiaryResponse
import retrofit2.Call
import java.io.File

interface MemoryTraceNetworkDataSource {

    fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        imageFile: File
    ): Call<PostDiaryResponse>

    fun editDiary(
        diaryId: Int,
        title: String,
        content: String,
        imageFile: File
    ): Call<PostDiaryResponse>
}