package com.upf.memorytrace_android.data.network

import com.upf.memorytrace_android.data.network.response.PostDiaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MemoryTraceService {

    @Multipart
    @POST("/diary")
    fun postDiary(
        @Part("bid") bookId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part imagePart: MultipartBody.Part
    ): Call<PostDiaryResponse>

    @Multipart
    @POST("/diary/update")
    fun editDiary(
        @Part("did") diaryId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part imagePart: MultipartBody.Part
    ): Call<Unit>
}