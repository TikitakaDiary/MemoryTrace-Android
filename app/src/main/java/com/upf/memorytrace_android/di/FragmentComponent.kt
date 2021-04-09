package com.upf.memorytrace_android.di

import androidx.fragment.app.Fragment
import dagger.Subcomponent

@Subcomponent
internal interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    fun inject(activity: Fragment)
}