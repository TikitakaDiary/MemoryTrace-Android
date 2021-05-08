package com.upf.memorytrace_android.viewmodel

import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.LiveEvent

internal class WriteViewModel : BaseViewModel() {
    val isLoadGallery = LiveEvent<Unit?>()
    val isAttachSticker = LiveEvent<Unit?>()

    fun loadGallery() {
        isLoadGallery.call()
    }

    fun attachSticker() {
        isAttachSticker.call()
    }
}