package com.upf.memorytrace_android.ui.diary.data.remote

import com.google.gson.annotations.SerializedName

data class PostDiaryResponse(
    @SerializedName("did") val diaryId: Int
)
