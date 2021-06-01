package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.LoginFragmentDirections
import kotlinx.coroutines.launch

internal class LoginViewModel : BaseViewModel() {
    val name = MutableLiveData("요기 터치")
    val api = MemoryTraceUtils.apiService()

    fun onClickWrite() {
        navDirections.value = LoginFragmentDirections.actionLoginFragmentToWriteFragment()
    }
}