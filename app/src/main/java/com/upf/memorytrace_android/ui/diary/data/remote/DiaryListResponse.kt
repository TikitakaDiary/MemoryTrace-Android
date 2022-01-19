package com.upf.memorytrace_android.ui.diary.data.remote

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList

data class DiaryListResponse(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("whoseTurn")
    val whoseTurn: Int,
    @SerializedName("diaryList")
    val diaryList: List<DiaryResponse>
)

fun DiaryListResponse.toEntity() = DiaryList(
    curPage = curPage,
    hasNext = hasNext,
    title = title,
    whoseTurn = whoseTurn,
    diaryList = diaryList.map(DiaryResponse::toEntity)
)