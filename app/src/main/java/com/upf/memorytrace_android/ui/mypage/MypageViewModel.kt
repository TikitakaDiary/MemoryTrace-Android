package com.upf.memorytrace_android.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
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

    val uid = MemoryTraceConfig.uid

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
            val response = UserRepository.withdrawalUser()
            when (response) {
                is NetworkState.Success -> resetUser()
                is NetworkState.Failure -> toast.value = response.message
            }
        }
    }

    fun joinToBook(code: String) {
        viewModelScope.launch {
            val response = UserRepository.joinToBook(code)
            when (response) {
                is NetworkState.Success -> {
                    toast.value = "일기가 생성되었습니다"
                    navDirections.value = BackDirections()
                }
                is NetworkState.Failure -> toast.value = response.message
            }
        }
    }

    fun onClickOssNotice() {
        showOssPage.call()
    }

    fun onClickSendEmail() {
        sendEmail.call()
    }

    /**
     * service : 1
     * privacy : 2
     */
    fun onClickTermService() {
        navDirections.value = MypageFragmentDirections.actionMypageFragmentToTermsFragment(1)
    }

    fun onClickTermPrivacy() {
        navDirections.value = MypageFragmentDirections.actionMypageFragmentToTermsFragment(2)
    }

    fun resetUser() {
        MemoryTraceConfig.clear()
        navDirections.value = MypageFragmentDirections.actionMypageFragmentToLoginFragment()
    }
}