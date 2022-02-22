package com.upf.memorytrace_android.ui.book.domain

import com.upf.memorytrace_android.api.model.User

data class Book(
    var bookId: Int,
    var nickname: String,
    var title: String,
    var backgroundColor: Int,
    var image: String,
    var date: String,
    var whoseTurn: Int,
    var inviteCode: String,
    var userList: List<User>
)