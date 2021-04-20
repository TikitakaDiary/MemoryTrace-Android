package com.upf.memorytrace_android.di

import android.content.Context
import android.util.Log
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.BuildConfig.DEBUG
import com.upf.memorytrace_android.api.MemoryTraceService
import com.upf.memorytrace_android.api.util.AuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager


@Module
class NetworkModule {
    val connectTimeout: Long = 60
    val readTimeout: Long = 60

    private val baseUrl = BuildConfig.BASE_URL

    @Provides
    @Singleton
    @Named("MemoryTrace")
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor(object :
                HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("OkHttp:KakaoHost", message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        okHttpClientBuilder.addInterceptor(AuthHeaderInterceptor())

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