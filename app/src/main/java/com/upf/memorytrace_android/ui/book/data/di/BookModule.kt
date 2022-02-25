package com.upf.memorytrace_android.ui.book.data.di

import com.upf.memorytrace_android.ui.book.data.BookRepositoryImpl
import com.upf.memorytrace_android.ui.book.data.BookService
import com.upf.memorytrace_android.ui.book.domain.BookRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class BookModule {

    @Provides
    fun provideBookService(@Named("MemoryTrace") retrofit: Retrofit): BookService =
        retrofit.create(BookService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface AbstractModule {
        @Binds
        fun bindsBookRepository(repository: BookRepositoryImpl): BookRepository
    }
}