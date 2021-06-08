package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.SplashFragmentDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SplashViewModel : BaseViewModel() {
    val name = MutableLiveData("스플래시")

    init {
        viewModelScope.launch {
            delay(1500L)
            navDirections.value = if (MemoryTraceConfig.isLoggedIn) {
                SplashFragmentDirections.actionSplashFragmentToBookListFragment()
            } else {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            }
        }
    }
}