package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.base.BaseViewModel

internal class LoginViewModel : BaseViewModel() {
    val name = MutableLiveData("로긘")
}