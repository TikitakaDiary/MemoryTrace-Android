package com.upf.memorytrace_android.ui.diary.data.di

import com.upf.memorytrace_android.ui.diary.data.DiaryRepositoryOldImpl
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepositoryOld
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Deprecated(
    "새로운 Retrofit 으로 주입하기 위해 해당 클래스는 deprecate 합니다.",
    ReplaceWith("DiaryModule", "com.upf.memorytrace_android.data.repository.DiaryModule")
)
@Module
@InstallIn(SingletonComponent::class)
class DiaryModuleOld {

    @Provides
    fun providesDiaryService(@Named("MemoryTrace") retrofit: Retrofit): DiaryService =
        retrofit.create(DiaryService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface AbstractModule {
        @Binds
        fun bindsDiaryRepository(repository: DiaryRepositoryOldImpl): DiaryRepositoryOld
    }
}