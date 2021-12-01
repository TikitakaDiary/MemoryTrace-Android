package com.upf.memorytrace_android

import android.app.Application
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
    }

    companion object {
        lateinit var instance: MemoryTraceApplication
        fun getApplication(): MemoryTraceApplication {
            return instance
        }
    }
}