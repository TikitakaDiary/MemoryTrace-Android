package com.upf.memorytrace_android.ui.mypage.themechange

import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.MemoryTraceConfig

internal class ThemeChangeViewModel : BaseViewModel() {
    var theme = MutableLiveData<Int>()

    init {
        theme.value = MemoryTraceConfig.theme
    }

    fun onClickLight() {
        theme.value = 1
    }

    fun onClickDark() {
        theme.value = 2
    }

    fun onClickDefault() {
        theme.value = 0
    }
}
