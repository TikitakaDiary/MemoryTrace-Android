package com.upf.memorytrace_android.data.di

import com.upf.memorytrace_android.common.di.qualifier.IoDispatcher
import com.upf.memorytrace_android.data.network.MemoryTraceNetworkDataSource
import com.upf.memorytrace_android.data.repository.DiaryRepository
import com.upf.memorytrace_android.data.repository.impl.DiaryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {

    @Provides
    @Singleton
    fun provideDiaryRepository(
        network: MemoryTraceNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DiaryRepository {
        return DiaryRepositoryImpl(
            network = network,
            ioDispatcher = ioDispatcher
        )
    }
}