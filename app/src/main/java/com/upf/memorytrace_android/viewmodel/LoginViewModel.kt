package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.base.BaseViewModel
import kotlinx.coroutines.launch

internal class LoginViewModel : BaseViewModel() {
    val name = MutableLiveData("로긘")
    val api = MemoryTraceUtils.apiService()

    init {
        viewModelScope.launch {
            name.value = api.fetchBooks(1)[0].toString()
        }

    }
}