package com.upf.memorytrace_android

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.upf.memorytrace_android.di.DaggerAppComponent


class MemoryTraceApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        SoLoader.init(this, false);

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(instance)) {
            AndroidFlipperClient.getInstance(instance).apply {
                addPlugin(InspectorFlipperPlugin(instance, DescriptorMapping.withDefaults()))
                addPlugin(NavigationFlipperPlugin.getInstance())
                addPlugin(DatabasesFlipperPlugin(instance))
                addPlugin(SharedPreferencesFlipperPlugin(instance))
                addPlugin(CrashReporterPlugin.getInstance())
                addPlugin(networkFlipperPlugin)
            }.start()
        }
    }

    companion object {
        internal val networkFlipperPlugin = NetworkFlipperPlugin()
        lateinit var instance: MemoryTraceApplication
        fun getApplication(): MemoryTraceApplication {
            return instance
        }
    }
}