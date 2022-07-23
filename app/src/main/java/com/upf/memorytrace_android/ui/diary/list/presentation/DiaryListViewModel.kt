package com.upf.memorytrace_android.ui.diary.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.util.onFailure
import com.upf.memorytrace_android.api.util.onSuccess
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.diary.list.domain.FetchDiariesUseCase
import com.upf.memorytrace_android.ui.diary.list.domain.FetchPinchInfoUseCase
import com.upf.memorytrace_android.ui.diary.list.domain.PinchInfo
import com.upf.memorytrace_android.ui.diary.list.domain.PinchUseCase
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
    private val fetchPinchInfoUseCase: FetchPinchInfoUseCase,
    private val pinchUseCase: PinchUseCase
) : ViewModel() {

    sealed class Event {
        data class WriteDiary(val bookId: Int) : Event()
        data class DiaryDetail(val diaryId: Int) : Event()
        data class Setting(val bookId: Int) : Event()
        data class Error(val errorMessage: String) : Event()
        data class SuccessPinch(val turnUserName: String) : Event()
    }

    private val _uiEvent = MutableEventLiveData<Event>()
    val uiEvent: EventLiveData<Event> = _uiEvent

    private val _diaryListUiState = MutableStateFlow(DiaryListUiState())
    val diaryListUiState: StateFlow<DiaryListUiState> = _diaryListUiState

    private val _diaryListTypeUiModel = MutableStateFlow(DiaryListTypeUiModel())
    val diaryListTypeUiModel: StateFlow<DiaryListTypeUiModel> = _diaryListTypeUiModel

    private val _diaryListContentUiModel = MutableStateFlow(DiaryListContentUiModel())
    val diaryListContentUiModel: StateFlow<DiaryListContentUiModel> = _diaryListContentUiModel

    private val _diaryHeaderUiModel = MutableStateFlow(DiaryHeaderUiModel())
    val diaryHeaderUiModel: StateFlow<DiaryHeaderUiModel> = _diaryHeaderUiModel

    private val fetchSize: Int
        get() = if (diaryListTypeUiModel.value.listType == DiaryListType.LINEAR) 10 else 30

    private var bookId: Int = -1
    private var page: Int = 0
    private var hasNext: Boolean = true

    private var lastScrollPositionOfLinearList = 0
    private var lastScrollPositionOfGridList = 0

    fun initializeDiaryList(bookId: Int) {
        this.bookId = bookId
        loadDiaryList(true)
    }

    fun loadDiaryList(force: Boolean = false) {
        val uiModel = diaryListUiState.value
        if (uiModel.isLoading) return
        if (force.not() && hasNext.not()) return

        if (force) {
            page = 0
            hasNext = true
        }

        viewModelScope.launch {
            _diaryListUiState.update { it.copy(isLoading = true) }

            fetchDiariesUseCase(bookId, ++page, fetchSize)
                .onSuccess {
                    val currentDiaryList = if (force) {
                        mutableListOf()
                    } else {
                        diaryListContentUiModel.value.diaries.toMutableList()
                    }

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
                    val isMyTurn = MemoryTraceConfig.uid == it.whoseTurn
                    hasNext = it.hasNext
                    _diaryListUiState.update { uiModel ->
                        uiModel.copy(
                            title = it.title,
                            isLoading = false,
                        )
                    }
                    _diaryListContentUiModel.update { uiModel ->
                        uiModel.copy(
                            diaries = currentDiaryList,
                            isMyTurn = isMyTurn,
                            isForce = force,
                        )
                    }
                    if (isMyTurn) {
                        _diaryHeaderUiModel.update { header ->
                            header.copy(
                                isLoading = false,
                                isMyTurn = true,
                                onMyTurnClick = this@DiaryListViewModel::writeDiary
                            )
                        }
                    } else {
                        fetchPinchInfo()
                    }
                }
                .onFailure {
                    _diaryListUiState.update { uiModel ->
                        uiModel.copy(
                            isLoading = false
                        )
                    }
                    _uiEvent.event = Event.Error(it)
                }
        }
    }

    fun writeDiary() {
        _uiEvent.event = Event.WriteDiary(bookId = bookId)
    }

    fun onClickSetting() {
        _uiEvent.event = Event.Setting(bookId)
    }

    fun changeListType() = viewModelScope.launch {
        _diaryListTypeUiModel.update {
            val newListType = it.listType.change()
            it.copy(
                listType = newListType,
                scrollPosition = if (newListType == DiaryListType.LINEAR) {
                    lastScrollPositionOfLinearList
                } else {
                    lastScrollPositionOfGridList
                }
            )
        }
    }

    private fun fetchPinchInfo() {
        viewModelScope.launch {
            _diaryHeaderUiModel.update { it.copy(isLoading = true) }
            fetchPinchInfoUseCase(bookId)
                .onSuccess {
                    it.onSuccess()
                }.onFailure {
                    onPinchInfoFailure(it)
                }
        }
    }

    private fun pinch() {
        viewModelScope.launch {
            _diaryHeaderUiModel.update { it.copy(isLoading = true) }
            pinchUseCase(bookId)
                .onSuccess {
                    _uiEvent.event = Event.SuccessPinch(it.turnUserName)
                    it.onSuccess()
                }.onFailure {
                    onPinchInfoFailure(it)
                }
        }
    }

    private fun PinchInfo.onSuccess() {
        _diaryHeaderUiModel.update { uiModel ->
            uiModel.copy(
                isMyTurn = false,
                isLoading = false,
                turnUserName = turnUserName,
                pinchable = pinchable,
                pinchCount = pinchCount,
                onPinchClick = { pinch() }
            )
        }
    }

    private fun onPinchInfoFailure(errorMessage: String) {
        _diaryHeaderUiModel.update { uiModel ->
            uiModel.copy(
                isLoading = false,
                isError = true
            )
        }
        _uiEvent.event = Event.Error(errorMessage)
    }

    private fun onClickDiaryDetail(diaryId: Int) {
        _uiEvent.event = Event.DiaryDetail(diaryId)
    }
}