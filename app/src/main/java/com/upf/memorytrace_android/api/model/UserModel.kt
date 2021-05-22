package com.upf.memorytrace_android.api.model


data class User(
    var uid: Int = -1,
    var nickname: String = "",
    var profilemg: String? = null,
    var jwt: String = ""
)