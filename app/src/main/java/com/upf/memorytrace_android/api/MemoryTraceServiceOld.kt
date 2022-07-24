package com.upf.memorytrace_android.api

import com.upf.memorytrace_android.api.model.*
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryListResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

@Deprecated(
    "새로운 Retrofit 으로 생성하기 위해 해당 클래스는 deprecated 합니다.\n" +
            "더 이상 추가하려면 MemoryTraceService 인터페이스에 추가하세요.",
    ReplaceWith("MemoryTraceService", "com.upf.memorytrace_android.data.network")
)
interface MemoryTraceServiceOld {

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
    ): BaseResponse<DiaryListResponse>
}