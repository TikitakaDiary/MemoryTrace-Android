package com.upf.memorytrace_android.ui.diary.data.remote

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.ui.diary.list.domain.Diary
import com.upf.memorytrace_android.util.TimeUtil
import java.util.*

data class DiaryResponse(
    @SerializedName("did")
    val diaryId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val imageUrl: String,
    @SerializedName("template")
    val template: Int,
    @SerializedName("createdDate")
    val createdDate: String
)

fun DiaryResponse.toEntity() = Diary(
    diaryId = diaryId,
    nickname = nickname,
    title = title,
    imageUrl = imageUrl,
    createdDate = TimeUtil.convertStringToDate(
        TimeUtil.FORMAT_yyyy_MM_dd_B_HH_mm_ss,
        createdDate
    ) ?: Calendar.getInstance().time
)