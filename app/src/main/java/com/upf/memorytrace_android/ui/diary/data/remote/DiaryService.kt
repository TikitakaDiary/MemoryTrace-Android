package com.upf.memorytrace_android.ui.diary.data.remote

import com.upf.memorytrace_android.api.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DiaryService {

    @GET("/diary/{did}")
    suspend fun fetchDiary(@Path("did") did: Int): BaseResponse<DiaryDetailResponse>
}