package com.upf.memorytrace_android.api.model

data class CreateBookModel(
    var whoseTurn: Int = -1,
    var title: String = "",
    var bgColor: Int = -1
)

data class Book(
    @SerializedName("bid") var id: Int = -1,
    var nickname: String = "",
    var title: String = "",
    var bgColor: Int = -1,
    @SerializedName("stickerImg") var image: String = "",
    @SerializedName("modifiedDate") var date: String = ""
)