package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.LoginFragmentDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.launch

internal class LoginViewModel : BaseViewModel() {

    val name = MutableLiveData("요기 터치")

    fun onClickWrite() {
        navDirections.value = LoginFragmentDirections.actionLoginFragmentToWriteFragment()
    }

    fun register(nickname: String, token: String, snsType: String) {
        viewModelScope.launch {
            //TODO: 응답 처리 필요
            UserRepository.createUser(nickname, token, snsType)
            navDirections.value = LoginFragmentDirections.actionLoginFragmentToWriteFragment()
        }
    }
}