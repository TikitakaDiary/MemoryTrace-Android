package com.upf.memorytrace_android.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LoginViewModel : BaseViewModel() {

    val name = MutableLiveData("요기 터치")

    fun register(nickname: String, snsKey: String, snsType: String, tempKey: String? = null) {
        viewModelScope.launch {
            var token = ""
            withContext(Dispatchers.Default) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        token = task.result ?: ""
                    }
                }
            }
            val response = UserRepository.createUser(nickname, snsKey, tempKey, snsType, token)
            when (response) {
                is NetworkState.Success -> navDirections.value =
                    LoginFragmentDirections.actionLoginFragmentToBookListFragment()
                is NetworkState.Failure -> toast.value = response.message
            }
        }
    }

    fun showTermsOfServiceFragment() {
        showTermsFragment(1)
    }

    fun showTermsOfPrivacyFragment() {
        showTermsFragment(2)
    }

    private fun showTermsFragment(terms: Int) {
        if (terms != 1 && terms != 2) return
        navDirections.value =
            LoginFragmentDirections.actionLoginFragmentToTermsFragment(terms)
    }
}