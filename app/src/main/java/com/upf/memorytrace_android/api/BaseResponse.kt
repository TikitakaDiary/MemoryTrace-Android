package com.upf.memorytrace_android.api

import com.upf.memorytrace_android.api.model.BookList
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
