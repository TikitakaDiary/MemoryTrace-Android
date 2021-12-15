package com.upf.memorytrace_android.di

import android.util.Log
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.MemoryTraceService
import com.upf.memorytrace_android.api.util.interceptor.AuthHeaderInterceptor
import com.upf.memorytrace_android.api.util.interceptor.StatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val connectTimeout: Long = 60
    private val readTimeout: Long = 60

    private val baseUrl = BuildConfig.BASE_URL

    @Provides
    @Singleton
    @Named("MemoryTrace")
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addNetworkInterceptor(HttpLoggingInterceptor { message ->
                Log.d("http", message)
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        okHttpClientBuilder.addInterceptor(AuthHeaderInterceptor())
        okHttpClientBuilder.addInterceptor(StatusInterceptor())

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @Named("MemoryTrace")
    fun provideRetrofit(@Named("MemoryTrace") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideMemoryTraceApi(@Named("MemoryTrace") retrofit: Retrofit): MemoryTraceService =
        retrofit.create(MemoryTraceService::class.java)

}