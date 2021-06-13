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
     * 초대코드로 일기장 입장
     */
    @POST("invite")
    suspend fun joinToBook(@Body inviteCode: String): BaseResponse


    /**
     * 회원가입 및 로그인
     * 200 : 이미 가입된 유저
     * 201 : 가입 성공
     */
    @POST("user")
    suspend fun createUser(@Body user: User): UserResponse

    @GET("user/withdrawal")
    suspend fun withdrawalUser(): BaseResponse

    @PUT("user")
    suspend fun editName(@Body user: UserName): BaseResponse

    /**
     * 다이어리
     */
    @GET("/diary/list/{bid}")
    suspend fun fetchDiaries(
        @Path("bid") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<DiaryListResponse>

    @GET("/diary/{did}")
    suspend fun fetchDiary(
        @Path("did") did: Int
    ): Response<DiaryResponse>

    @Multipart
    @POST("/diary")
    suspend fun createDiary(
        @Query("bid") bid: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part img: MultipartBody.Part
    ): Response<DiaryCreateResponse>
}