package com.upf.memorytrace_android.viewmodel

import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.LiveEvent

internal class WriteViewModel : BaseViewModel() {
    val isAttachSticker = LiveEvent<Unit?>()

    fun attachSticker() {
        isAttachSticker.call()
    }
}