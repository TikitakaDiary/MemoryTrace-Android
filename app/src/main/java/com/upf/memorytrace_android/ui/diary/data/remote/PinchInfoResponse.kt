package com.upf.memorytrace_android.ui.diary.data.remote

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.ui.diary.list.domain.PinchInfo

data class PinchInfoResponse(
    @SerializedName("nickname") val turnUserName: String,
    val pinchable: Boolean,
    @SerializedName("pinchCnt") val pinchCount: Int
)

fun PinchInfoResponse.toEntity() = PinchInfo(
    turnUserName = turnUserName,
    pinchCount = pinchCount
)