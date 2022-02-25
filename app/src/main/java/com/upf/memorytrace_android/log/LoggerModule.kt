package com.upf.memorytrace_android.log

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LoggerModule {

    @Provides
    fun providesExceptionLogger(): ExceptionLogger = FirebaseExceptionLogger()
}