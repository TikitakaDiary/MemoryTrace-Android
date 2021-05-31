package com.upf.memorytrace_android.api

import com.upf.memorytrace_android.api.model.BookList

open class BaseResponse {
    val statusCode: String = ""
    val responseMessage: String = ""

    open val isSuccess: Boolean
        get() = statusCode == "201"
}


class BookResponse : BaseResponse() {
    val data: BookList? = null
}
