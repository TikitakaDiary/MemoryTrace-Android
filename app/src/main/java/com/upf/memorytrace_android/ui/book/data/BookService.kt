package com.upf.memorytrace_android.ui.book.data

import com.upf.memorytrace_android.api.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookService {

    @PUT("book/exit/{bookId}")
    suspend fun leaveBook(@Path("bookId") bookId: Int): BaseResponse<*>

    @GET("book/{bid}")
    suspend fun fetchBook(
        @Path("bid") bookId: Int
    ): BaseResponse<BookResponse>
}