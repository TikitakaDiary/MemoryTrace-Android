package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.book.BookSettingFragmentArgs
import com.upf.memorytrace_android.ui.book.BookSettingFragmentDirections
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.LiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class BookSettingViewModel : BaseViewModel() {

    private var bid: Int = -1
    val showLeaveDialog = LiveEvent<Unit?>()

    init {
        viewModelScope.launch {
            navArgs<BookSettingFragmentArgs>()
                .collect {
                    bid = it.bid
                    if (bid < 0) {
                        toast.value = ERROR_ARGS
                        onClickBack()
                    }
                }
        }
    }

    fun onClickMember() {
        navDirections.value =
            BookSettingFragmentDirections.actionBookSettingFragmentToMemberSettingFragment(bid)
    }

    fun onClickUpdateBook() {
        navDirections.value =
            BookSettingFragmentDirections.actionBookSettingFragmentToCreateBookFragment(bid)
    }

    fun onclickLeaveBook() {
        showLeaveDialog.call()
    }

    fun leaveBook() {
        viewModelScope.launch {
            val response = BookRepository.leaveBook(bid)
            when (response) {
                is NetworkState.Success -> {
                    navDirections.value =
                        BookSettingFragmentDirections.actionBookSettingFragmentToBookListFragment()
                }
                is NetworkState.Failure -> toast.value = response.message
            }
        }
    }

    fun onClickBack() {
        navDirections.postValue(BackDirections())
    }

    companion object {
        private const val ERROR_ARGS = "잘못된 접근입니다. 다시 시도해주세요."
    }
}