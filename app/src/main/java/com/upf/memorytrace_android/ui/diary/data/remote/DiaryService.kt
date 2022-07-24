package com.upf.memorytrace_android.ui.diary.data.remote

import com.upf.memorytrace_android.api.model.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

@Deprecated(
    "API 함수는 MemoryTraceService 에 모두 모을 예정입니다.",
    ReplaceWith("MemoryTraceService", "com.upf.memorytrace_android.data.network")
)
interface DiaryService {

    @GET("/diary/{did}")
    suspend fun fetchDiary(@Path("did") did: Int): BaseResponse<DiaryDetailResponse>

    @GET("/pinch/{bookId}")
    suspend fun fetchPinchInfo(@Path("bookId") bookId: Int): BaseResponse<PinchInfoResponse>

    @POST("/pinch/{bookId}")
    suspend fun pinch(@Path("bookId") bookId: Int): BaseResponse<Unit?>
}
