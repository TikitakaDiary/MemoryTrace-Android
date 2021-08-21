package com.upf.memorytrace_android.api

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.api.model.*

open class BaseResponse {
    val statusCode: String = ""
    val responseMessage: String = ""

    open val isSuccess: Boolean
        get() = statusCode == "201" || statusCode == "200"
}

data class BookResponse(
    @SerializedName("data")
    val data: Book
) : BaseResponse()

class BookListResponse : BaseResponse() {
    val data: BookList? = null
}

class UserResponse : BaseResponse() {
    val data: User? = null
}

data class DiaryListResponse(
    @SerializedName("data")
    val data: DiaryListModel
) : BaseResponse()

data class DiaryResponse(
    @SerializedName("data")
    val data: DiaryDetailModel
) : BaseResponse()

data class DiaryCreateResponse(
    @SerializedName("data")
    val data: DiaryCreateModel
) : BaseResponse()
