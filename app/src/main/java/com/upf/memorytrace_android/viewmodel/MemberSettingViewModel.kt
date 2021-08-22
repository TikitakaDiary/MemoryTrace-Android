package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.MemberSettingFragmentArgs
import com.upf.memorytrace_android.ui.book.BookSettingFragmentArgs
import com.upf.memorytrace_android.ui.book.CreateBookFragmentArgs
import com.upf.memorytrace_android.util.BackDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MemberSettingViewModel : BaseViewModel() {

    val invite = MutableLiveData<String>()
    private var code = ""

    init {
        viewModelScope.launch {
            navArgs<MemberSettingFragmentArgs>()
                .map {
                    BookRepository.fetchBook(it.bid)
                }.collect { response ->
                    when (response) {
                        is NetworkState.Success -> {
                            code = response.data?.inviteCode ?: ""
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