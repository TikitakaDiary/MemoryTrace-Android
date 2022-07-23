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
    ): Call<PostDiaryResponse>
}
