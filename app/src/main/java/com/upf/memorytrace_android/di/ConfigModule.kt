package com.upf.memorytrace_android.di

import com.upf.memorytrace_android.util.MemoryTraceConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Singleton
    @Provides
    fun provideMemoryTraceConfig(): MemoryTraceConfig {
        return MemoryTraceConfig
    }
}
