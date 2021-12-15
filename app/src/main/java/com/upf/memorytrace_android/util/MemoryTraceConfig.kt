package com.upf.memorytrace_android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.MemoryTraceApplication
import java.time.format.SignStyle

object MemoryTraceConfig {
    private const val PREFERENCE = "MemoryTrace.preference"

    private const val UID = "uid"
    private const val NICKNAME = "nickname"
    private const val USER_TOKEN = "user_token"
    private const val SIGNUP_SNS = "signup_sns"
    private const val SIGNUP_DATE = "signup_date"
    private const val PROFILE_IMG = "profile_img"
    private const val BID = "bid"
    private const val DID = "did"

    private const val SAVE_DEBUG_KEY = "save_debug_key"

    fun setCrashlyticsCustomKeys() {
        FirebaseCrashlytics.getInstance().setUserId(uid.toString())
        FirebaseCrashlytics.getInstance().setCustomKey(SIGNUP_SNS, sns ?: "")
        FirebaseCrashlytics.getInstance().setCustomKey(SIGNUP_DATE, signupDate ?: "")
        saveDebugKey = true
    }

    private val pref: SharedPreferences =
        MemoryTraceApplication.getApplication()
            .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

    var uid: Int
        get() = pref.getInt(UID, 0)
        set(value) = pref.edit { putInt(UID, value) }

    var nickname: String?
        get() = pref.getString(NICKNAME, "")
        set(value) = pref.edit { putString(NICKNAME, value) }

    //TODO: 로그인 api에서 토큰 받고 그걸 서버로 보내서 jwt로 저장, jwt가 만료되면 다시 진행 ..? 음 리프레시 토큰?
    var token: String?
        get() = pref.getString(USER_TOKEN, "")
        set(value) = pref.edit { putString(USER_TOKEN, value) }

    var sns: String?
        get() = pref.getString(SIGNUP_SNS, "")
        set(value) = pref.edit { putString(SIGNUP_SNS, value) }

    var signupDate: String?
        get() = pref.getString(SIGNUP_DATE, "")
        set(value) = pref.edit { putString(SIGNUP_DATE, value) }

    var profileImg: String?
        get() = pref.getString(PROFILE_IMG, "")
        set(value) = pref.edit { putString(PROFILE_IMG, value) }

    var isLoggedIn: Boolean = !nickname.isNullOrBlank() && !token.isNullOrBlank()

    var saveDebugKey: Boolean?
        get() = pref.getBoolean(SAVE_DEBUG_KEY, false)
        set(value) = pref.edit { putBoolean(SAVE_DEBUG_KEY, value ?: false) }

    fun clear() {
        pref.edit { clear() }
    }
}