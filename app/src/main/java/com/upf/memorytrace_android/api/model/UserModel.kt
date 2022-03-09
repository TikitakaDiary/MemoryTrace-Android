package com.upf.memorytrace_android.api.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class User(
    var uid: Int = -1,
    var nickname: String = "",
    var profileImg: String? = null,
    var jwt: String = "",
    var snsKey: String = "",
    var tempKey: String? = "",
    var snsType: String = "",
    var createdDate: String = "",
    var token: String = ""
):Parcelable

data class UserName(var nickname: String = "")
