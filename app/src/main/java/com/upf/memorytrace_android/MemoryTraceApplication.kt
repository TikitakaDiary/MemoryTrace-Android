package com.upf.memorytrace_android

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.upf.memorytrace_android.di.DaggerAppComponent
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.ThemeUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MemoryTraceApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        if (BuildConfig.DEBUG) {
            Firebase.analytics.setUserId("Developer")
        }
        ThemeUtil.applyTheme()
    }

    companion object {
        lateinit var instance: MemoryTraceApplication
        fun getApplication(): MemoryTraceApplication {
            return instance
        }
    }
}
