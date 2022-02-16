package com.upf.memorytrace_android.ui.diary.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.onFailure
import com.upf.memorytrace_android.onSuccess
import com.upf.memorytrace_android.ui.diary.list.domain.FetchDiariesUseCase
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val fetchDiariesUseCase: FetchDiariesUseCase,
) : ViewModel() {

    sealed class Event {
        data class WriteDiary(val bookId: Int) : Event()
        data class DiaryDetail(val diaryId: Int) : Event()
        data class Setting(val bookId: Int) : Event()
    }

    private val _uiEvent = MutableEventLiveData<Event>()
    val uiEvent: EventLiveData<Event> = _uiEvent

    private val _uiState = MutableStateFlow(DiaryListUiState())
    val uiState: StateFlow<DiaryListUiState> = _uiState

    private val fetchSize: Int
        get() = if (uiState.value.listType == DiaryListType.LINEAR) 10 else 30

    fun initializeDiaryList(bookId: Int) {
        _uiState.update { it.copy(bookId = bookId) }
        loadDiaryList(true)
    }

    fun loadDiaryList(force: Boolean = false) {
        val uiModel = uiState.value
        if ((force.not() && uiModel.isLoading) || uiModel.hasNext.not()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            fetchDiariesUseCase(uiModel.bookId, uiModel.page + 1, fetchSize)
                .onSuccess {
                    val currentDiaryList = uiModel.diaries.toMutableList()

                    it.diaryList.forEach { currentItem ->
                        // 조건에 따라 DateItem 삽입
                        if (currentDiaryList.isEmpty()) {
                            currentDiaryList.add(
                                DiaryListItem.DateItem(currentItem.createdDate)
                            )
                        } else {
                            val lastItem = currentDiaryList.last()
                            if (lastItem is DiaryListItem.DiaryItem) {
                                val monthOfLastItem =
                                    TimeUtil.getMonth(lastItem.createdDate)
                                val monthOfCurrentItem =
                                    TimeUtil.getMonth(currentItem.createdDate)

                                if (monthOfLastItem != monthOfCurrentItem) {
                                    currentDiaryList.add(
                                        DiaryListItem.DateItem(currentItem.createdDate)
                                    )
                                }
                            }
                        }
                        // DiaryItem 삽입
                        currentDiaryList.add(
                            currentItem.toPresentEntity(
                                onItemClick = { onClickDiaryDetail(currentItem.diaryId) }
                            )
                        )
                    }
                    _uiState.update { uiModel ->
                        uiModel.copy(
                            title = it.title,
                            diaries = currentDiaryList,
                            hasNext = it.hasNext,
                            page = uiModel.page + 1,
                            isMyTurn = MemoryTraceConfig.uid == it.whoseTurn,
                            isForce = force,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.emit(
                        uiModel.copy(
                            isLoading = false,
                            isFailure = true,
                            errorMessage = it
                        )
                    )
                }
        }
    }

    fun writeDiary() {
        _uiEvent.event = Event.WriteDiary(uiState.value.bookId)
    }

    fun onClickSetting() {
        _uiEvent.event = Event.Setting(uiState.value.bookId)
    }

    fun changeListType() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                listType = it.listType.change()
            )
        }
    }

    private fun onClickDiaryDetail(diaryId: Int) {
        _uiEvent.event = Event.DiaryDetail(diaryId)
    }
}