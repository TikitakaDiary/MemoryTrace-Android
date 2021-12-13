package com.upf.memorytrace_android.ui.diary.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.detail.domain.FetchDiaryUseCase
import com.upf.memorytrace_android.ui.diary.write.DiaryDetailDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchDiaryUseCase: FetchDiaryUseCase
) : ViewModel() {

    sealed class Event {
        object CommentList: Event()
        data class EditDiary(val diaryDetailDTO: DiaryDetailDTO): Event()
        data class Toast(val content: String): Event()
    }

    private val _isShowBottomLayout = MutableStateFlow(true)
    val isShowBottomLayout: StateFlow<Boolean> = _isShowBottomLayout

    private val _uiEvent = MutableEventLiveData<Event>()
    val uiEvent: EventLiveData<Event> = _uiEvent

    private val _uiState = MutableStateFlow<UiState<DiaryDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<DiaryDetail>> = _uiState

    fun fetchDiaryDetail(diaryId: Int) {
        viewModelScope.launch {
            _uiState.emit(fetchDiaryUseCase(diaryId))
        }
    }

    fun toggleBottomLayout() {
        _isShowBottomLayout.value = _isShowBottomLayout.value.not()
    }

    fun setBottomLayoutVisibility(isVisible: Boolean) {
        if (isVisible != _isShowBottomLayout.value) {
            _isShowBottomLayout.value = isVisible
        }
    }

    fun updateDiary() {
        val uiState = uiState.value
        if (uiState is UiState.Success) {
            val diaryDetail = uiState.data
            if (diaryDetail.isModifiable) {
                _uiEvent.event = Event.EditDiary(DiaryDetailDTO.from(diaryDetail))
            } else {
                _uiEvent.event = Event.Toast("일기장 수정은 작성 후 30분 내에만 가능해요!")
            }
        }
    }

    fun showCommentList() {
        _uiEvent.event = Event.CommentList
    }
}