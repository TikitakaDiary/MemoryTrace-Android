package com.upf.memorytrace_android.ui.splash

import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SplashViewModel : BaseViewModel() {
    init {
        viewModelScope.launch {
            delay(1500L)
            navDirections.value = if (!MemoryTraceConfig.isLoggedIn) {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            } else {
                if (MemoryTraceConfig.saveDebugKey == false)
                    MemoryTraceConfig.setCrashlyticsCustomKeys()

                SplashFragmentDirections.actionSplashFragmentToBookListFragment()
            }
        }
    }
}