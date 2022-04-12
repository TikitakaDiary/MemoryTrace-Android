package com.upf.memorytrace_android.api.util

import com.upf.memorytrace_android.api.model.BaseResponse
import java.io.IOException

class StatusError(response: BaseResponse<*>) : IOException() {
    val statusCode = response.statusCode
    val responseMessage = response.responseMessage ?: ""
}