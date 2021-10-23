package com.upf.memorytrace_android.ui.diary.write.image

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class CameraImageModule {

    @Provides
    fun provideCameraImageDelegate(@ApplicationContext context: Context): CameraImageDelegate =
        CameraImageDelegate(context)
}