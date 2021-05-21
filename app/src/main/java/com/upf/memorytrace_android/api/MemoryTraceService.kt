package com.upf.memorytrace_android.api

import com.upf.memorytrace_android.api.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface MemoryTraceService {

    /**
     * 다이어리 생성
     */
    @Multipart
    @POST("book")
    suspend fun createBook(
        @Part("title") title: String,
        @Part("bgColor") bgColor: Int,
        @Part stickerImg: MultipartBody.Part? = null
    ): BaseResponse

    /**
     * 다이어리 목록 조회
     */
    @GET("book/list")
    suspend fun fetchBooks(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BookResponse

    /**
     * 초대코드 생성
     */
    @POST("invite")
    suspend fun createInviteLink(@Body inviteModel: InviteModel): BaseResponse

    /**
     * 회원가입
     */
    @POST("user")
    suspend fun createUser(@Body userModel: UserModel): BaseResponse

}