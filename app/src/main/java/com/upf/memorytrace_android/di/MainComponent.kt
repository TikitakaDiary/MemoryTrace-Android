package com.upf.memorytrace_android.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Subcomponent

@Subcomponent
internal interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: AppCompatActivity)
}