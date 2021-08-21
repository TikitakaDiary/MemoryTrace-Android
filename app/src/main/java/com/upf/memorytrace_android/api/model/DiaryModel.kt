package com.upf.memorytrace_android.api.model

import com.google.gson.annotations.SerializedName

data class DiaryCreateModel(
    @SerializedName("did")
    val did: Int
)

data class DiaryListModel(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("whoseTurn")
    val whoseTurn: Int,
    @SerializedName("diaryList")
    val diaryList: List<DiaryModel>
)

data class DiaryModel(
    @SerializedName("did")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("template")
    val template: Int,
    @SerializedName("createdDate")
    val createdDate: String
)

data class DiaryDetailModel(
    @SerializedName("did")
    val id: Int,
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
    val createdDate: String
)