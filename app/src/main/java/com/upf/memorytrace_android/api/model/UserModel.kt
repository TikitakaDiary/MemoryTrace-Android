package com.upf.memorytrace_android.api.model


data class User(
    var uid: Int = -1,
    var nickname: String = "",
    var profilemg: String? = null,
    var jwt: String = "",
    var snsKey: String = "",
    var snsType: String = "",
    var createdDate: String = "",
    var token: String = ""
)

data class UserName(var nickname: String = "")
