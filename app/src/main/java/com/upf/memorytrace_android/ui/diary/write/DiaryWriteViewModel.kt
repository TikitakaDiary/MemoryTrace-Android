package com.upf.memorytrace_android.ui.diary.write

import androidx.lifecycle.ViewModel
import com.upf.memorytrace_android.color.UserColor
import com.upf.memorytrace_android.common.MutableStateStackFlow
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.diary.write.color.ColorItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DiaryWriteViewModel : ViewModel() {

    private val _toolbarState: MutableStateStackFlow<DiaryWriteToolbarState> =
        MutableStateStackFlow(DiaryWriteToolbarState.WRITE)
    val toolbarState: StateFlow<DiaryWriteToolbarState>
        get() = _toolbarState

    private val _contentUiModel: MutableStateStackFlow<DiaryWriteContentUiModel> =
        MutableStateStackFlow(DiaryWriteContentUiModel())
    val contentUiModel: StateFlow<DiaryWriteContentUiModel>
        get() = _contentUiModel

    private val _selectColorUiModel: MutableStateFlow<DiaryWriteSelectColorUiModel> =
        MutableStateFlow(DiaryWriteSelectColorUiModel())
    val selectColorUiModel: StateFlow<DiaryWriteSelectColorUiModel>
        get() = _selectColorUiModel

    private val _errorEvent: MutableEventLiveData<DiaryWriteErrorEvent> =
        MutableEventLiveData()
    val errorEvent: EventLiveData<DiaryWriteErrorEvent>
        get() = _errorEvent

    private var diaryId: Int? = null
    var isNewDiary: Boolean = true
        private set

    fun loadDiary(diaryId: Int?, originalDiary: DiaryWriteContentUiModel?) {
        if (diaryId != null && originalDiary != null) {
            this.diaryId = diaryId
            isNewDiary = false
            _contentUiModel.update { originalDiary }
            _toolbarState.push { DiaryWriteToolbarState.EDIT }
        } else {
            this.diaryId = null
            isNewDiary = true
            _toolbarState.push { DiaryWriteToolbarState.WRITE }
        }
    }

    fun restorePreviousToolbarState() {
        _toolbarState.pop()
    }

    fun showCancelConfirmDialogIfNeed() {
        // Todo: 변경사항 있는지 보고 이벤트에 반영
    }

    fun onClickSelectColorButton() {
        if (contentUiModel.value.image is WriteImageType.Image) {
            // Todo : 이미지 날아가는데 괜찮냐고 묻기
        } else {
            showSelectColorLayout()
        }
    }

    fun onSaveSelectColorClick() {
        restorePreviousToolbarState()
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
    }

    fun dismissSelectColorLayout() {
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
        _contentUiModel.update { it.copy(image = selectColorUiModel.value.previousImageType) }
    }

    private fun showSelectColorLayout() {
        _toolbarState.push { DiaryWriteToolbarState.SELECT_COLOR }
        val previousImageType = contentUiModel.value.image

        if (previousImageType is WriteImageType.Color) {
            val previousSelectedColor = previousImageType.color
            _selectColorUiModel.setSelectedColorAndShow(previousSelectedColor)
        } else {
            _contentUiModel.setColorToDefault()
            _selectColorUiModel.setSelectedColorAndShow(DEFAULT_SELECTED_COLOR)
        }
    }

    private fun onSelectColorItem(selectedColor: UserColor) {
        val newColorList = fetchUserColors().map { userColor ->
            userColor.toColorItemUiModel(isSelected = userColor == selectedColor)
        }
        _selectColorUiModel.update { it.copy(colorList = newColorList) }
        _contentUiModel.update { it.copy(image = WriteImageType.Color(selectedColor)) }
    }

    private fun MutableStateFlow<DiaryWriteContentUiModel>.setColorToDefault() {
        update {
            it.copy(image = WriteImageType.Color(DEFAULT_SELECTED_COLOR))
        }
    }

    private fun MutableStateFlow<DiaryWriteSelectColorUiModel>.setSelectedColorAndShow(
        selectedColor: UserColor
    ) {
        update {
            DiaryWriteSelectColorUiModel(
                colorList = fetchUserColors().map { userColor ->
                    userColor.toColorItemUiModel(isSelected = (userColor == selectedColor))
                },
                isShowing = true,
                previousImageType = contentUiModel.value.image
            )
        }
    }

    private fun UserColor.toColorItemUiModel(isSelected: Boolean): ColorItemUiModel {
        return ColorItemUiModel(
            color = this,
            isSelected = isSelected,
            onSelectColorItem = {
                onSelectColorItem(this)
            }
        )
    }

    private fun fetchUserColors(): List<UserColor> {
        return UserColor.getAllColors()
    }

    companion object {
        private val DEFAULT_SELECTED_COLOR = UserColor.SYSTEM_GRAY
    }
}