package com.upf.memorytrace_android.ui.diary.data.remote

import com.upf.memorytrace_android.api.model.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface DiaryService {

    @GET("/diary/{did}")
    suspend fun fetchDiary(@Path("did") did: Int): BaseResponse<DiaryDetailResponse>

    @GET("/pinch/{bookId}")
    suspend fun fetchPinchInfo(@Path("bookId") bookId: Int): BaseResponse<PinchInfoResponse>

    @POST("/pinch/{bookId}")
    suspend fun pinch(@Path("bookId") bookId: Int): BaseResponse<Unit?>

    @Multipart
    @POST("/diary")
    fun postDiary(
        @Query("bid") bid: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part img: MultipartBody.Part
    ): Call<PostDiaryResponse>
}
