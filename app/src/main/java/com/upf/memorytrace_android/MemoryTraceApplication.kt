package com.upf.memorytrace_android

import android.app.Application
import com.upf.memorytrace_android.di.DaggerAppComponent

internal class MemoryTraceApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}