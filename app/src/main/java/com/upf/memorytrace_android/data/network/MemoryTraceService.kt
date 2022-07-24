package com.upf.memorytrace_android.data.network

import com.upf.memorytrace_android.data.network.response.PostDiaryResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MemoryTraceService {

    @Multipart
    @POST("/diary")
    fun postDiary(
        @Query("bid") bookId: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part imagePart: MultipartBody.Part
    ): Call<PostDiaryResponse>

    @Multipart
    @POST("/diary/update")
    fun editDiary(
        @Query("did") diaryId: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part imagePart: MultipartBody.Part
    ): Call<Unit>
}