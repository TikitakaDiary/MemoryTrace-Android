package com.upf.memorytrace_android.api.model

import com.google.gson.annotations.SerializedName

data class BookList(
    var curPage: Int = 1,
    var hasNext: Boolean = false,
    var bookList: List<Book>? = null
)

data class Book(
    @SerializedName("bid") var id: Int = -1,
    var nickname: String = "",
    var title: String = "",
    var bgColor: Int = -1,
    @SerializedName("stickerImg") var image: String = "",
    @SerializedName("modifiedDate") var date: String = ""
)