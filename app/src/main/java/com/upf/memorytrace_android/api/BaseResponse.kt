package com.upf.memorytrace_android.api

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.api.model.BookList
import com.upf.memorytrace_android.api.model.DiaryCreateModel
import com.upf.memorytrace_android.api.model.DiaryListModel
import com.upf.memorytrace_android.api.model.User

open class BaseResponse {
    val statusCode: String = ""
    val responseMessage: String = ""

    open val isSuccess: Boolean
        get() = statusCode == "201" || statusCode == "200"
}


class BookResponse : BaseResponse() {
    val data: BookList? = null
}

class UserResponse : BaseResponse() {
    val data: User? = null
}

data class DiaryResponse(
    @SerializedName("data")
    val data: DiaryListModel
) : BaseResponse()

data class DiaryCreateResponse(
    @SerializedName("data")
    val data: DiaryCreateModel
) : BaseResponse()
