package com.upf.memorytrace_android.ui.book.data

import com.google.gson.annotations.SerializedName
import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.ui.book.domain.Book

data class BookResponse(
    @SerializedName("bid") var bookId: Int = -1,
    var nickname: String = "",
    var title: String = "",
    @SerializedName("bgColor") var backgroundColor: Int = -1,
    @SerializedName("stickerImg") var image: String = "",
    @SerializedName("modifiedDate") var date: String = "",
    var whoseTurn: Int = -1,
    var inviteCode: String = "",
    var userList: List<User> = emptyList()
)

fun BookResponse.toEntity() = Book(
    bookId = bookId,
    nickname = nickname,
    title = title,
    backgroundColor = backgroundColor,
    image = image,
    date = date,
    whoseTurn = whoseTurn,
    inviteCode = inviteCode,
    userList = userList
)