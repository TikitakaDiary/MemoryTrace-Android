package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.base.BaseViewModel
import javax.inject.Inject

internal class MainViewModel @Inject constructor() : BaseViewModel() {
    val name = MutableLiveData("테스트")
}