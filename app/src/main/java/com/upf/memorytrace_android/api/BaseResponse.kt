package com.upf.memorytrace_android.api

open class BaseResponse {
    val statusCode: String = ""
    val statusCodeValue: Int = 0
    val message:String = ""
    val body:Object? = null

    open val isSuccess: Boolean
        get() = statusCode == "100"
}
