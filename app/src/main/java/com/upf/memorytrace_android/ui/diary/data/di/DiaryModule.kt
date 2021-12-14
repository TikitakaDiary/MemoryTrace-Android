package com.upf.memorytrace_android.ui.diary.data.di

import com.upf.memorytrace_android.ui.diary.data.DiaryRepositoryImpl
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DiaryModule {

    @Provides
    fun providesDiaryService(@Named("MemoryTrace") retrofit: Retrofit): DiaryService =
        retrofit.create(DiaryService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface AbstractModule {
        @Binds
        fun bindsDiaryRepository(repository: DiaryRepositoryImpl): DiaryRepository
    }
}