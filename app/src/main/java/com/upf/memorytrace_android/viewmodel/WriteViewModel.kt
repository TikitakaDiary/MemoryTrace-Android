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
    val isAttachSticker = LiveEvent<Unit?>()

    fun showSelectImgDialog() {
        isShowSelectImgDialog.call()
    }

    fun loadGallery() {
        isLoadGallery.call()
    }

    fun loadCamera() {
        isLoadCamera.call()
    }

    fun attachSticker() {
        isAttachSticker.call()
    }
}