package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.BookSettingFragmentDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.launch

internal class BookSettingViewModel : BaseViewModel() {
    var book = Book()
    val bid = 1

    init {
        viewModelScope.launch {
            val response = BookRepository.fetchBookSetting(bid)
            //todo; 일기장 정보를 가져와서 그려야하는데, 어떤 api를 써야하나 ...
        }
    }

    fun onClickMember() {
        navDirections.value =
            BookSettingFragmentDirections.actionBookSettingFragmentToMemberSettingFragment()
    }

    fun onClickLeave() {
        viewModelScope.launch {
            BookRepository.exitBook(bid)
            navDirections.value =
                BookSettingFragmentDirections.actionBookSettingFragmentToBookListFragment()
        }
    }
}