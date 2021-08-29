package com.upf.memorytrace_android.viewmodel

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.write.WriteFragmentArgs
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.ImageConverter
import com.upf.memorytrace_android.util.LiveEvent
import com.xiaopo.flying.sticker.Sticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

internal class WriteViewModel : BaseViewModel() {
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

    companion object {
        private const val EMPTY_STRING = ""
    }
}