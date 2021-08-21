package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.SplashFragmentDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SplashViewModel : BaseViewModel() {
    init {
        viewModelScope.launch {
            delay(1500L)
            navDirections.value = if (!MemoryTraceConfig.isLoggedIn) {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            } else if (MemoryTraceConfig.bid != -1) {
                val bid = MemoryTraceConfig.bid
                MemoryTraceConfig.bid = -1
                SplashFragmentDirections.actionSplashFragmentToDiaryFragment(bid)
            } else {
                SplashFragmentDirections.actionSplashFragmentToBookListFragment()

            }
        }
    }
}