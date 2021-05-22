package com.upf.memorytrace_android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.upf.memorytrace_android.MemoryTraceApplication

object MemoryTraceConfig {
    private const val PREFERENCE = "MemoryTrace.preference"
    private const val NICKNAME = "nickname"
    private const val USER_TOKEN = "user_token"

    private val pref: SharedPreferences =
        MemoryTraceApplication.getApplication()
            .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

    var nickname: String?
        get() = pref.getString(NICKNAME, "")
        set(value) = pref.edit { putString(NICKNAME, value) }

    //TODO: 로그인 api에서 토큰 받고 그걸 서버로 보내서 jwt로 저장, jwt가 만료되면 다시 진행 ..? 음 리프레시 토큰?
    var token: String?
        get() = pref.getString(USER_TOKEN, "")
        set(value) = pref.edit { putString(USER_TOKEN, value) }

    var isLoggedIn: Boolean = !nickname.isNullOrBlank() && !token.isNullOrBlank()
}