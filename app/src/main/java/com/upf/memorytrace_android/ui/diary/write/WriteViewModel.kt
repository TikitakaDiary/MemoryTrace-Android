package com.upf.memorytrace_android.ui.diary.write

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.ui.diary.write.image.CameraImageDelegate
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.ImageConverter
import com.upf.memorytrace_android.util.LiveEvent
import com.xiaopo.flying.sticker.Sticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

internal class WriteViewModel(
    private val cameraImageDelegate: CameraImageDelegate
) : BaseViewModel() {
    val bitmap = MutableLiveData<Bitmap?>()
    val color = MutableLiveData<Colors?>()
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val isShowColorDialog = MutableLiveData<Boolean>()

    val isShowSelectImgDialog = LiveEvent<Unit?>()
    val isLoadGallery = LiveEvent<Unit?>()
    val isLoadCamera = LiveEvent<Unit?>()

    val isShowStickerDialog = LiveEvent<Boolean>()
    val addSticker = LiveEvent<Int>()

    val isSaveDiary = LiveEvent<Unit?>()
    val isExit = LiveEvent<Unit?>()

    private var _imageUrl: Uri? = null
    val imageUri : Uri?
        get() = _imageUrl

    private var bid = -1
    private var originalBackgroundColor: Colors? = null
    var stickerList = mutableListOf<Sticker>()

    init {
        viewModelScope.launch {
            navArgs<WriteFragmentArgs>()
                .collect {
                    bid = it.bid
                }
        }
    }

    fun showSelectImgDialog() {
        isShowSelectImgDialog.call()
    }

    fun loadGallery() {
        isLoadGallery.call()
    }

    fun loadCamera() {
        isLoadCamera.call()
    }

    fun showStickerDialog() {
        isShowStickerDialog.value = true
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
        isShowColorDialog.value = false
    }

    fun changeColor(c: Colors) {
        color.value = c
    }

    fun saveDiary() {
        isSaveDiary.call()
    }

    fun uploadDiary(cacheDir: File, bitmap: Bitmap) {
        viewModelScope.launch {
            ImageConverter.convertBitmapToMultipartBody(bitmap, cacheDir, null, null)?.let {
                val response = DiaryRepository.createDiary(
                    bid,
                    title.value ?: EMPTY_STRING,
                    content.value ?: EMPTY_STRING,
                    it
                )
            if (title.value?.trim().isNullOrBlank()) {
                toast.value = EMPTY_TITLE
                showLoading.value = false
                return@launch
            }

            if (bitmap.value == null && color.value == null) {
                toast.value = EMPTY_IMAGE
                showLoading.value = false
                return@launch
            }

                when (response) {
                    is NetworkState.Success -> onClickBack()
                    is NetworkState.Failure -> toast.value = response.message
                }
            }
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