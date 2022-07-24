package com.upf.memorytrace_android.ui.diary.write

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.onFailure
import com.upf.memorytrace_android.api.onSuccess
import com.upf.memorytrace_android.api.util.apiErrorMessage
import com.upf.memorytrace_android.color.UserColor
import com.upf.memorytrace_android.data.repository.DiaryRepository
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.diary.write.color.ColorItemUiModel
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    private val memoryTraceConfig: MemoryTraceConfig,
    private val repository: DiaryRepository,
) : ViewModel() {

    private val _toolbarState: MutableStateFlow<DiaryWriteToolbarState> =
        MutableStateFlow(DiaryWriteToolbarState.WRITE)
    val toolbarState: StateFlow<DiaryWriteToolbarState>
        get() = _toolbarState

    private val _contentUiModel: MutableStateFlow<DiaryWriteContentUiModel> =
        MutableStateFlow(DiaryWriteContentUiModel())
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

    private var bookId: Int? = null
    private var diaryId: Int? = null
    private var originalDiary: DiaryWriteContentUiModel = DiaryWriteContentUiModel()
    var isNewDiary: Boolean = true
        private set

    var currentCameraImageFileUri: Uri? = null
        private set

    fun setBookId(bookId: Int) {
        isNewDiary = true
        this.bookId = bookId
        _contentUiModel.update {
            it.copy(
                writerName = memoryTraceConfig.nickname.orEmpty(),
                date = TimeUtil.getTodayDate(TimeUtil.YYYY_M_D_KR)
            )
        }
        this.originalDiary = contentUiModel.value
        _toolbarState.update { DiaryWriteToolbarState.WRITE }
    }

    fun loadOriginalDiary(diaryId: Int, originalDiary: DiaryWriteContentUiModel) {
        this.diaryId = diaryId
        this.originalDiary = originalDiary
        isNewDiary = false
        _contentUiModel.update { originalDiary }
        _toolbarState.update { DiaryWriteToolbarState.EDIT }
    }

    fun onSaveSelectColorClick() {
        clearCameraTempFileUri()
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
    }

    fun dismissSelectColorLayout() {
        _contentUiModel.update { it.copy(image = selectColorUiModel.value.previousImageType) }
        _selectColorUiModel.update { DiaryWriteSelectColorUiModel(isShowing = false) }
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
        val previousImageType = contentUiModel.value.image

        if (previousImageType is WriteImageType.Color) {
            val previousSelectedColor = previousImageType.color
            _selectColorUiModel.setSelectedColorAndShow(previousSelectedColor)
        } else {
            _selectColorUiModel.setSelectedColorAndShow(UserColor.getDefaultColor())
            _contentUiModel.setColorToDefault()
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
            it.copy(image = WriteImageType.Color(UserColor.getDefaultColor()))
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

    fun hasDiaryDiff(): Boolean {
        return contentUiModel.value != originalDiary
    }

    fun onBackPressed() {
        val isShowingSelectColorLayout = selectColorUiModel.value.isShowing
        if (isShowingSelectColorLayout) {
            dismissSelectColorLayout()
        } else {
            if (hasDiaryDiff()) {
                _event.event = DiaryWriteEvent.ShowFinishConfirmDialog
            } else {
                clearBackUpData()
                _event.event = DiaryWriteEvent.FinishWriteActivity
            }
        }
    }

    fun clearBackUpData() {
        // Todo
    }

    fun onTitleChanged(title: String) {
        _contentUiModel.update { it.copy(title = title) }
    }

    fun onContentChanged(content: String) {
        _contentUiModel.update { it.copy(content = content) }
    }

    fun postDiary(imageFile: File) {
        val bookId = bookId ?: return
        viewModelScope.launch {
            val model = contentUiModel.value
            repository.postDiary(bookId, model.title, model.content, imageFile)
                .onSuccess {
                    _event.event = DiaryWriteEvent.PostDone
                }.onFailure {
                    FirebaseCrashlytics.getInstance().recordException(it)
                    _errorEvent.event = DiaryWriteErrorEvent.FailPost(it.apiErrorMessage())
                }
        }
    }

    fun editedDiary(imageFile: File) {
        val diaryId = diaryId ?: return
        viewModelScope.launch {
            val model = contentUiModel.value
            repository.editDiary(diaryId, model.title, model.content, imageFile)
                .onSuccess {
                    _event.event = DiaryWriteEvent.EditDone
                }.onFailure {
                    FirebaseCrashlytics.getInstance().recordException(it)
                    _errorEvent.event = DiaryWriteErrorEvent.FailPost(it.apiErrorMessage())
                }
        }
    }

    fun onStickerItemSelected(drawable: Drawable) {
        _contentUiModel.update { it.copy(stickerEdited = true) }
        _event.event = DiaryWriteEvent.AddSticker(drawable)
    }
}