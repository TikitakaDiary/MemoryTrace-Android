package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.launch

internal class NameEditViewModel : BaseViewModel() {
    var name = MutableLiveData<String>()

    init {
        name.value = MemoryTraceConfig.nickname
    }

    fun editName(name: String) {
        viewModelScope.launch {
            UserRepository.editName(name)
            MemoryTraceConfig.nickname = name
        }
    }
}