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
    ): BaseResponse<*>

    @Multipart
    @POST("book/update")
    suspend fun updateBook(
        @Part("bid") bid: Int,
        @Part("title") title: String,
        @Part("bgColor") bgColor: Int,
        @Part stickerImg: MultipartBody.Part? = null
    ): BaseResponse<*>

    /**
     * 다이어리 목록 조회
     */
    @GET("book/list")
    suspend fun fetchBooks(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<BookList>

    @GET("book/{bid}")
    suspend fun fetchBook(
        @Path("bid") bid: Int
    ): BaseResponse<Book>

    @PUT("book/exit/{bid}")
    suspend fun leaveBook(
        @Path("bid") bid: Int
    ): BaseResponse<*>


    /**
     * 초대코드로 일기장 입장
     */
    @POST("invite")
    suspend fun joinToBook(@Body inviteCode: String): BaseResponse<*>


    /**
     * 회원가입 및 로그인
     * 200 : 이미 가입된 유저
     * 201 : 가입 성공
     */
    @POST("user")
    suspend fun createUser(@Body user: User): BaseResponse<User>

    @GET("user/withdrawal")
    suspend fun withdrawalUser(): BaseResponse<*>

    @PUT("user")
    suspend fun editName(@Body user: UserName): BaseResponse<*>

    @POST("user/fcm")
    suspend fun registerFcmToken(@Body user: User): BaseResponse<*>

    /**
     * 다이어리
     */
    @GET("/diary/list/{bid}")
    suspend fun fetchDiaries(
        @Path("bid") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<DiaryListModel>

    @Multipart
    @POST("/diary")
    suspend fun createDiary(
        @Query("bid") bid: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part img: MultipartBody.Part
    ): BaseResponse<*>

    @Multipart
    @POST("/diary/update")
    suspend fun modifyDiary(
        @Query("did") did: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Part img: MultipartBody.Part
    ): BaseResponse<*>
}