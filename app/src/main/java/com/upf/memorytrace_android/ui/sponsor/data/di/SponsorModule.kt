package com.upf.memorytrace_android.ui.sponsor.data.di

import com.upf.memorytrace_android.ui.sponsor.data.SponsorRepositoryImpl
import com.upf.memorytrace_android.ui.sponsor.data.SponsorService
import com.upf.memorytrace_android.ui.sponsor.domain.SponsorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class SponsorModule {

    @Provides
    fun providesSponsorService(@Named("MemoryTrace") retrofit: Retrofit): SponsorService =
        retrofit.create(SponsorService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface AbstractModule {
        @Binds
        fun bindsSponsorRepository(repository: SponsorRepositoryImpl): SponsorRepository
    }
}