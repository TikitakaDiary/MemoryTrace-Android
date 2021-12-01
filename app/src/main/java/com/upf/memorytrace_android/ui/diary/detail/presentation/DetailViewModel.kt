package com.upf.memorytrace_android.ui.diary.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.detail.domain.FetchDiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchDiaryUseCase: FetchDiaryUseCase
) : ViewModel() {

    enum class Event {
        COMMENT_LIST
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
        //Todo: 리베이스 후 수정
//        if(enableUpdate.value == true){
//            navDirections.value = DetailFragmentDirections.actionDetailFragmentToWriteFragment(-1, diary)
//        }else{
//            //todo: 출력안됨.
//            toast.value = "일기장 수정은 작성 후 30분 내에만 가능해요!"
//        }
    }

    fun showCommentList() {
        _uiEvent.event = Event.COMMENT_LIST
    }
}