package com.upf.memorytrace_android.ui.book.setting.member

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.BackDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MemberSettingViewModel : BaseViewModel() {
    val invite = MutableLiveData<String>()
    val userList = MutableLiveData<List<User>>()
    private var code = ""

    private var bid = -1

    init {
        viewModelScope.launch {
            navArgs<MemberSettingFragmentArgs>()
                .map {
                    BookRepository.fetchBook(it.bid)
                }.collect { response ->
                    when (response) {
                        is NetworkState.Success -> {
                            code = response.data.inviteCode
                            userList.postValue(response.data.userList)
                        }
                        is NetworkState.Failure -> {
                            toast.value = response.message
                            onClickBack()
                        }
                    }
                }
        }
    }

    fun onClickInvite() {
        invite.value = code
        toast.value = CODE_COPIED
    }

    fun onClickBack() {
        navDirections.postValue(BackDirections())
    }

    companion object {
        private const val CODE_COPIED = "코드가 복사되었습니다."
    }
}