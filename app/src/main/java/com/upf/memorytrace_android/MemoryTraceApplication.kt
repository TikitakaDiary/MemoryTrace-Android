package com.upf.memorytrace_android

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.upf.memorytrace_android.di.DaggerAppComponent
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
    }

    companion object {
        lateinit var instance: MemoryTraceApplication
        fun getApplication(): MemoryTraceApplication {
            return instance
        }
    }
}