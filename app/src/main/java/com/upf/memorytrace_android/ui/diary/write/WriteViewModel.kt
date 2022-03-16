package com.upf.memorytrace_android.ui.diary.write

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.ui.diary.write.image.CameraImageDelegate
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.ImageConverter
import com.upf.memorytrace_android.util.LiveEvent
import com.xiaopo.flying.sticker.Sticker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class WriteViewModel @Inject constructor(
    private val cameraImageDelegate: CameraImageDelegate
) : BaseViewModel() {
    var showLoading = MutableLiveData<Boolean>(false)

    val bitmap = MutableLiveData<Bitmap?>()
    val cantEditableImageUrl = MutableLiveData<String?>()
    val color = MutableLiveData<Colors?>()
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val isShowColorDialog = MutableLiveData<Boolean>()

    val showCantEditable = LiveEvent<Unit?>()
    val isShowSelectImgDialog = LiveEvent<Unit?>()
    val isLoadGallery = LiveEvent<Unit?>()
    val isLoadCamera = LiveEvent<Unit?>()

    val isShowStickerDialog = LiveEvent<Boolean>()
    val addSticker = LiveEvent<Int>()

    val isSaveDiary = LiveEvent<Unit?>()
    val isExit = LiveEvent<Unit?>()

    val isWriteDone = LiveEvent<Unit>()

    private var _imageUrl: Uri? = null
    val imageUri: Uri?
        get() = _imageUrl

    private var bid = -1
    private var did = -1
    private var originalBackgroundColor: Colors? = null
    var stickerList = mutableListOf<Sticker>()

    init {
        viewModelScope.launch {
            navArgs<WriteFragmentArgs>()
                .collect {
                    bid = it.bid
                    it.diary?.let { diary ->
                        setDiary(diary)
                    }
                }
        }
    }

    var isCreateMode: Boolean = true

    private fun setDiary(diary: DiaryDetailDTO) {
        did = diary.diaryId
        if (did != -1) isCreateMode = false

        title.value = diary.title
        content.value = diary.content
        cantEditableImageUrl.value = diary.imageUrl
    }

    fun showSelectImgDialog() {
        if (!isCreateMode && cantEditableImageUrl.value != null) {
            showCantEditable.call()
        } else {
            isShowSelectImgDialog.call()
        }
    }

    fun resetImages() {
        cantEditableImageUrl.value = null
        bitmap.value = null
        color.value = null
    }

    fun loadGallery() {
        isLoadGallery.call()
    }

    fun loadCamera() {
        isLoadCamera.call()
    }

    fun showStickerDialog() {
        if (!isCreateMode && cantEditableImageUrl.value != null) {
            showCantEditable.call()
        } else {
            isShowStickerDialog.value = true
        }
    }

    fun closeStickerDialog() {
        isShowStickerDialog.value = false
    }

    fun attachSticker(@DrawableRes stickerId: Int) {
        addSticker.value = stickerId
    }

    fun showColorDialog() {
        originalBackgroundColor = color.value
        isShowColorDialog.value = true
    }

    fun dismissColorDialog() {
        color.value = originalBackgroundColor
        isShowColorDialog.value = false
    }

    fun saveColor() {
        //사전에 color가 설정되기 때문에 reset 함수를 쓸 수 없음
        cantEditableImageUrl.value = null
        bitmap.value = null
        isShowColorDialog.value = false
    }

    fun changeColor(c: Colors) {
        color.value = c
    }

    fun saveDiary() {
        isSaveDiary.call()
    }

    fun uploadDiary(cacheDir: File, image: Bitmap) {
        viewModelScope.launch {
            showLoading.value = true
            if (title.value?.trim().isNullOrBlank()) {
                toast.value = EMPTY_TITLE
                showLoading.value = false
                return@launch
            }

            if (bitmap.value == null && color.value == null && cantEditableImageUrl.value == null) {
                toast.value = EMPTY_IMAGE
                showLoading.value = false
                return@launch
            }

            ImageConverter.convertBitmapToMultipartBody(image, cacheDir, null, null)?.let {
                val response = if (isCreateMode) {
                    //create diary
                    DiaryRepository.createDiary(
                        bid,
                        title.value ?: EMPTY_STRING,
                        content.value ?: EMPTY_STRING,
                        it
                    )
                } else {
                    //update diary
                    DiaryRepository.modifyDiary(
                        did,
                        title.value ?: EMPTY_STRING,
                        content.value ?: EMPTY_STRING,
                        it
                    )
                }

                when (response) {
                    is NetworkState.Success -> isWriteDone.call()
                    is NetworkState.Failure -> toast.value = response.message
                }
            }
            showLoading.value = false
        }
    }

    fun onClickExit() {
        isExit.call()
    }

    fun onClickBack() {
        navDirections.postValue(BackDirections())
    }

    fun getImageNewImageUri(): Uri {
        return cameraImageDelegate.createImageUri().also { _imageUrl = it }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_TITLE = "제목을 입력해 주세요!"
        private const val EMPTY_IMAGE = "사진이나 색상을 지정해 주세요!"
    }
}