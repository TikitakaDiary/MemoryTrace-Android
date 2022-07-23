package com.upf.memorytrace_android.data.di

import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.util.converter.MemoryTraceConverterFactory
import com.upf.memorytrace_android.api.util.interceptor.AuthHeaderInterceptor
import com.upf.memorytrace_android.data.network.MemoryTraceNetworkDataSource
import com.upf.memorytrace_android.data.network.MemoryTraceService
import com.upf.memorytrace_android.data.network.impl.MemoryTraceNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMemoryTraceNetworkDataSource(): MemoryTraceNetworkDataSource {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(AuthHeaderInterceptor())
            .build()
        val service = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MemoryTraceConverterFactory.create())
            .build()
            .create(MemoryTraceService::class.java)
        return MemoryTraceNetworkDataSourceImpl(service = service)
    }
}