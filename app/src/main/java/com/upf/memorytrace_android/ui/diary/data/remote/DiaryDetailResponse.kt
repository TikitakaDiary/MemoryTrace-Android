package com.upf.memorytrace_android.ui.diary.data.remote

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.util.TimeUtil

data class DiaryDetailResponse(
    @SerializedName("modifiable")
    val isModifiable: Boolean,
    @SerializedName("did")
    val diaryId: Int,
    @SerializedName("uid")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("template")
    val template: Int,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("commentCnt")
    val commentCount: Int
)

fun DiaryDetailResponse.toEntry(): DiaryDetail? {
    val date = TimeUtil.convertStringToDate(TimeUtil.FORMAT_yyyy_MM_dd_B_HH_mm_ss, createdDate)?: return null
    return DiaryDetail(
        isModifiable = isModifiable,
        title = title,
        content = content,
        nickname = nickname,
        imageUrl = img,
        date = TimeUtil.getDate(TimeUtil.YYYY_M_D_KR, date),
        commentCount = commentCount
    )
}