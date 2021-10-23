package com.upf.memorytrace_android.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(subcomponents = [ActivityComponent::class, FragmentComponent::class])
@InstallIn(SingletonComponent::class)
internal class SubComponentModule