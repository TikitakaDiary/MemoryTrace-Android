package com.upf.memorytrace_android.data.network.response

import com.google.gson.annotations.SerializedName

data class PostDiaryResponse(
    @SerializedName("did") val diaryId: Int
)
