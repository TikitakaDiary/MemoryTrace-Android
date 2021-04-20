package com.upf.memorytrace_android.api

import com.upf.memorytrace_android.api.model.BookModel
import com.upf.memorytrace_android.api.model.CreateBookModel
import com.upf.memorytrace_android.api.model.InviteModel
import com.upf.memorytrace_android.api.model.UserModel
import retrofit2.Response
import retrofit2.http.*


interface MemoryTraceService {

    /**
     * 다이어리 생성
     */
    @POST("book")
    suspend fun createBook(@Body createBookModel: CreateBookModel): BaseResponse

    /**
     * 다이어리 목록 조회
     */
    @GET("book/list/{uid}")
    suspend fun fetchBooks(@Path("uid") uid: Int): List<BookModel>

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