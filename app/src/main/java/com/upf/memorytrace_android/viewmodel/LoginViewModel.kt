package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.LoginFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LoginViewModel : BaseViewModel() {

    val name = MutableLiveData("요기 터치")

    fun register(nickname: String, snsKey: String, snsType: String) {
        viewModelScope.launch {
            var token = ""
            withContext(Dispatchers.Default) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        token = task.result ?: ""
                    }
                }
            }
            //TODO: 응답 처리 필요
            // todo: token 실패시 처리 필요
            UserRepository.createUser(nickname, snsKey, snsType, token)
            navDirections.value = LoginFragmentDirections.actionLoginFragmentToBookListFragment()
        }
    }
}