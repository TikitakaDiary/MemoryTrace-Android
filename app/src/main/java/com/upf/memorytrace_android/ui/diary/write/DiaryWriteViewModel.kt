package com.upf.memorytrace_android.ui.diary.write

import android.net.Uri
import androidx.core.net.toFile
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

    private val _contentUiModel: MutableStateFlow<DiaryWriteContentUiModel> =
        MutableStateStackFlow(DiaryWriteContentUiModel())
    val contentUiModel: StateFlow<DiaryWriteContentUiModel>
        get() = _contentUiModel

    private val _selectColorUiModel: MutableStateFlow<DiaryWriteSelectColorUiModel> =
        MutableStateFlow(DiaryWriteSelectColorUiModel())
    val selectColorUiModel: StateFlow<DiaryWriteSelectColorUiModel>
        get() = _selectColorUiModel

    private val _event = MutableEventLiveData<DiaryWriteEvent>()
    val event: EventLiveData<DiaryWriteEvent>
        get() = _event

    private val _errorEvent: MutableEventLiveData<DiaryWriteErrorEvent> =
        MutableEventLiveData()
    val errorEvent: EventLiveData<DiaryWriteErrorEvent>
        get() = _errorEvent

    private var diaryId: Int? = null
    var isNewDiary: Boolean = true
        private set

    var currentCameraImageFileUri: Uri? = null
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

    fun onSaveSelectColorClick() {
        clearCameraTempFileUri()
        restorePreviousToolbarState()
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
    }

    fun dismissSelectColorLayout() {
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
        _contentUiModel.update { it.copy(image = selectColorUiModel.value.previousImageType) }
    }

    fun onClickSelectCameraType() {
        if (contentUiModel.value.image is WriteImageType.Color) {
            _event.event = DiaryWriteEvent.ShowEditConfirmDialog(SelectImageType.CAMERA)
        } else {
            startCameraActivity()
        }
    }

    fun onClickSelectAlbumType() {
        if (contentUiModel.value.image is WriteImageType.Color) {
            _event.event = DiaryWriteEvent.ShowEditConfirmDialog(SelectImageType.ALBUM)
        } else {
            startGalleryActivity()
        }
    }

    fun onClickSelectColorType() {
        if (contentUiModel.value.image is WriteImageType.Image) {
            _event.event = DiaryWriteEvent.ShowEditConfirmDialog(SelectImageType.COLOR)
        } else {
            showSelectColorLayout()
        }
    }

    fun onClickEditConfirmYes(nextImageType: SelectImageType) {
        when (nextImageType) {
            SelectImageType.CAMERA -> startCameraActivity()
            SelectImageType.ALBUM -> startGalleryActivity()
            SelectImageType.COLOR -> showSelectColorLayout()
        }
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
    private fun startCameraActivity() {
        _event.event = DiaryWriteEvent.StartCameraActivity
    }

    private fun startGalleryActivity() {
        _event.event = DiaryWriteEvent.StartGalleryActivity
    }

    fun applyContentImage(imageUri: Uri) {
        clearCameraTempFileUri()
        _contentUiModel.update {
            it.copy(image = WriteImageType.Image(imageUri))
        }
    }

    fun saveCameraTempFileUri(uri: Uri) {
        clearCameraTempFileUri()
        currentCameraImageFileUri = uri
    }

    // 호출되어야 하는 상황
    // 1. 카메라 촬영에 실패했을 때
    // 2. 최종적으로 이미지가 적용되었을 때 (크롭되어 새로운 파일이 적용됨)
    // 3. 새로운 임시 파일을 생성하기 전에
    fun clearCameraTempFileUri() {
        currentCameraImageFileUri?.let {
            val file = it.toFile()
            if (file.exists()) {
                file.delete()
            }
            currentCameraImageFileUri = null
        }
    }

    companion object {
        private val DEFAULT_SELECTED_COLOR = UserColor.SYSTEM_GRAY
    }
}