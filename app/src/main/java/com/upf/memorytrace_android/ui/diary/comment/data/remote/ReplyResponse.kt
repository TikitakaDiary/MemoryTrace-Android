package com.upf.memorytrace_android.ui.diary.comment.data.remote

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment

data class ReplyResponse(
    @SerializedName("cid") val commentId: Int,
    @SerializedName("uid") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("isDelete") val isDeleted: Int
)

fun ReplyResponse.toEntity() =
    Comment(commentId, userId, nickname, content, createdDate, isDeleted, true)