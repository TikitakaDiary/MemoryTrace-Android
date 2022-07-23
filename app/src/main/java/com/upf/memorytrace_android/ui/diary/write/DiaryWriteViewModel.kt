package com.upf.memorytrace_android.ui.diary.write

import androidx.lifecycle.ViewModel
import com.upf.memorytrace_android.common.MutableStateStackFlow
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DiaryWriteViewModel : ViewModel() {

    private val _diaryWriteUiModel: MutableStateFlow<DiaryWriteUiModel> =
        MutableStateFlow(DiaryWriteUiModel())
    val diaryWriteUiModel: StateFlow<DiaryWriteUiModel>
        get() = _diaryWriteUiModel

    private val _diaryDiaryWriteToolbarState: MutableStateStackFlow<DiaryWriteToolbarState> =
        MutableStateStackFlow(DiaryWriteToolbarState.WRITE)
    val diaryDiaryWriteToolbarState: StateFlow<DiaryWriteToolbarState>
        get() = _diaryDiaryWriteToolbarState

    private val _diaryWriteErrorEvent: MutableEventLiveData<DiaryWriteErrorEvent> =
        MutableEventLiveData()
    val diaryWriteErrorEvent: EventLiveData<DiaryWriteErrorEvent>
        get() = _diaryWriteErrorEvent

    private var diaryId: Int? = null
    var isNewDiary: Boolean = true
        private set

    fun loadDiary(diaryId: Int?, originalDiary: DiaryWriteUiModel?) {
        if (diaryId != null && originalDiary != null) {
            this.diaryId = diaryId
            isNewDiary = false
            _diaryWriteUiModel.update { originalDiary }
            _diaryDiaryWriteToolbarState.push { DiaryWriteToolbarState.EDIT }
        } else {
            this.diaryId = null
            isNewDiary = true
            _diaryDiaryWriteToolbarState.push { DiaryWriteToolbarState.WRITE }
        }
    }

    fun restorePreviousToolbarState() {
        _diaryDiaryWriteToolbarState.pop()
    }

    fun showCancelConfirmDialogIfNeed() {
        // Todo: 변경사항 있는지 보고 이벤트에 반영
    }
}