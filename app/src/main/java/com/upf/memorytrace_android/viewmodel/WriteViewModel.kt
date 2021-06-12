package com.upf.memorytrace_android.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.LiveEvent

internal class WriteViewModel : BaseViewModel() {
    val bitmap = MutableLiveData<Bitmap?>()
    val color = MutableLiveData<Colors?>()

    val isShowSelectImgDialog = LiveEvent<Unit?>()
    val isLoadGallery = LiveEvent<Unit?>()
    val isLoadCamera = LiveEvent<Unit?>()

    val isShowStickerDialog = LiveEvent<Unit?>()
    val addSticker = LiveEvent<Int>()

    val isShowColorDialog = LiveEvent<Unit?>()

    val isSaveDiary = LiveEvent<Unit?>()

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
        isShowStickerDialog.call()
    }

    fun attachSticker() {
        addSticker.call()
    }

    fun showColorDialog() {
        isShowColorDialog.call()
    }

    fun changeColor(c: Colors) {
        color.value = c
    }

    fun saveDiary() {
        isSaveDiary.call()
    }

    fun uploadDiary(bitmap: Bitmap) {

    }
}