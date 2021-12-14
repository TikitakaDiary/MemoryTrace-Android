package com.upf.memorytrace_android.ui.diary.comment.data.di

import com.upf.memorytrace_android.ui.diary.comment.data.CommentRepositoryImpl
import com.upf.memorytrace_android.ui.diary.comment.data.remote.CommentService
import com.upf.memorytrace_android.ui.diary.comment.domain.CommentRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class CommentModule {

    @Provides
    fun providesCommentService(@Named("MemoryTrace") retrofit: Retrofit): CommentService =
        retrofit.create(CommentService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface AbstractModule {
        @Binds
        fun bindsCommentRepository(repository: CommentRepositoryImpl): CommentRepository
    }
}