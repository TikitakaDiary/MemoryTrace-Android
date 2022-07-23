package com.upf.memorytrace_android.di

import android.content.Context
import com.upf.memorytrace_android.api.MemoryTraceServiceOld
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class, ViewModelFactoryModule::class,
    SubComponentModule::class,
    NetworkModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory
    fun fragmentComponent(): FragmentComponent.Factory

    fun getApi():MemoryTraceServiceOld
}