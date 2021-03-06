package com.upf.memorytrace_android.ui.mypage.nameedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
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
           val response =  UserRepository.editName(name)

            when (response) {
                is NetworkState.Success -> {
                    MemoryTraceConfig.nickname = name
                    toast.value = "닉네임이 변경되었습니다."
                    navDirections.value = BackDirections()
                }
                is NetworkState.Failure -> toast.value = response.message
            }

        }
    }
}