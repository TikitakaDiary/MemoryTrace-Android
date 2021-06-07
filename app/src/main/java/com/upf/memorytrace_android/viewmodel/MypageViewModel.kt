package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.MypageFragmentDirections
import com.upf.memorytrace_android.ui.book.BookListFragmentDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.launch

internal class MypageViewModel : BaseViewModel() {
    val name = MutableLiveData<String>()
    val sns = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val version = MutableLiveData<String>()

    init {
        name.value = MemoryTraceConfig.nickname
        sns.value = MemoryTraceConfig.sns
        date.value = MemoryTraceConfig.signupDate
        version.value = BuildConfig.VERSION_NAME
    }
    fun withdrawalUser() {
        viewModelScope.launch {
            UserRepository.withdrawalUser()
        }
    }
}