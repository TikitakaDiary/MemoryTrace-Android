package com.upf.memorytrace_android.api.util

import com.upf.memorytrace_android.api.model.BaseResponse
import java.io.IOException

class StatusError(response: BaseResponse<*>) : IOException(response.responseMessage) {
    val statusCode: String = response.statusCode
    val responseMessage: String = response.responseMessage ?: ""
}