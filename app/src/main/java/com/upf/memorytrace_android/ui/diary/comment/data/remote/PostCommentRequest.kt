package com.upf.memorytrace_android.ui.diary.comment.data.remote

import com.google.gson.annotations.SerializedName

data class PostCommentRequest(
    @SerializedName("parent") val parentCommentId: Int?,
    @SerializedName("did") val diaryId: Int,
    @SerializedName("content") val content: String
)
