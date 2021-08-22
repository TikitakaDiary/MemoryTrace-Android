package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.MypageFragmentDirections
import com.upf.memorytrace_android.ui.book.BookListFragmentDirections
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.LiveEvent
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.launch

internal class MypageViewModel : BaseViewModel() {
    val name = MutableLiveData<String>()
    val sns = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val version = MutableLiveData<String>()

    val showOssPage = LiveEvent<Unit?>()
    val sendEmail = LiveEvent<Unit?>()

    init {
        name.value = MemoryTraceConfig.nickname
        sns.value = MemoryTraceConfig.sns
        date.value = MemoryTraceConfig.signupDate
        version.value = BuildConfig.VERSION_NAME
    }

    fun onClickMakers() {
        navDirections.value =
            MypageFragmentDirections.actionMypageFragmentToMakersFragment()
    }

    fun withdrawalUser() {
        viewModelScope.launch {
            UserRepository.withdrawalUser()
            resetUser()
        }
    }

    fun joinToBook(code: String) {
        viewModelScope.launch {
            val response = UserRepository.joinToBook(code)
            toast.value = "일기가 생성되었습니다"
            navDirections.value = BackDirections()
        }
    }

    fun onClickOssNotice(){
        showOssPage.call()
    }

    fun onClickSendEmail(){
        sendEmail.call()
    }

    fun resetUser() {
        MemoryTraceConfig.clear()
        navDirections.value = MypageFragmentDirections.actionMypageFragmentToLoginFragment()
    }
}