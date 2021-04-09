package com.upf.memorytrace_android.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Subcomponent

@Subcomponent
internal interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(activity: AppCompatActivity)
}