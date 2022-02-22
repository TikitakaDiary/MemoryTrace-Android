package com.upf.memorytrace_android.ui.book.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.util.onFailure
import com.upf.memorytrace_android.api.util.onSuccess
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.book.domain.FetchBookUseCase
import com.upf.memorytrace_android.ui.book.setting.domain.LeaveBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookSettingViewModel @Inject constructor(
    private val leaveBookUseCase: LeaveBookUseCase,
    private val fetchBookUseCase: FetchBookUseCase
) : ViewModel() {

    sealed class Event {
        object Leave: Event()
        object LeaveDone: Event()
        data class ShowMember(val bookId: Int): Event()
        data class EditBook(val bookId: Int): Event()
        data class Error(val message: String): Event()
        object WrongAccess: Event()
        data class CopyInvitationCode(val invitationCode: String): Event()
    }

    private var bookId: Int = -1

    private val _uiEvent = MutableEventLiveData<Event>()
    val uiEvent: EventLiveData<Event>
        get() = _uiEvent

    fun setBookId(bookId: Int) {
        if (bookId < 0) {
            _uiEvent.event = Event.WrongAccess
        } else {
            this.bookId = bookId
        }
    }

    fun onClickMember() {
        _uiEvent.event = Event.ShowMember(bookId)
    }

    fun onClickEditBook() {
        _uiEvent.event = Event.EditBook(bookId)
    }

    fun onClickLeaveBook() {
        _uiEvent.event = Event.Leave
    }

    fun onClickCopyInvitationCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fetchBookUseCase(bookId)
            }.onSuccess {
                _uiEvent.event = Event.CopyInvitationCode(it.inviteCode)
            }.onFailure {
                _uiEvent.event = it.takeIf { it.isNotEmpty() }?.let { message ->
                    Event.Error(message)
                }?: Event.WrongAccess
            }
        }
    }

    fun leaveBook() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                leaveBookUseCase(bookId)
            }.onSuccess {
                _uiEvent.event = Event.LeaveDone
            }.onFailure {
                _uiEvent.event = it.takeIf { it.isNotEmpty() }?.let { message ->
                    Event.Error(message)
                }?: Event.WrongAccess
            }
        }
    }
}