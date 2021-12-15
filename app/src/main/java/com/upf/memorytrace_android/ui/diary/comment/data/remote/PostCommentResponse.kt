package com.upf.memorytrace_android.ui.diary.comment.data.remote

import com.google.gson.annotations.SerializedName

data class PostCommentResponse(
    @SerializedName("cid") val commentId: Int
)
