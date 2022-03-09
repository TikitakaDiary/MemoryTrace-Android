package com.upf.memorytrace_android.ui.login.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.ui.login.LoginFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TermsAgreeViewModel @Inject constructor(
//    private val memberRepository: MemberRepository
) : ViewModel() {
    val showError = MutableLiveData("")
    val loginSuccess = MutableLiveData(false)

    val moveToTermsAgreePage = MutableLiveData(0)

    fun register(member: User) {
//        viewModelScope.launch {
//            val response = memberRepository.signup(member)
//            when (response) {
//                is NetworkState.Success -> loginSuccess.value = true
//                is NetworkState.Failure -> showError.value = response.message
//            }
//        }
    }

    fun showTermsOfServiceFragment() {
        moveToTermsAgreePage.value = 1
    }

    fun showTermsOfPrivacyFragment() {
        moveToTermsAgreePage.value = 2
    }

}