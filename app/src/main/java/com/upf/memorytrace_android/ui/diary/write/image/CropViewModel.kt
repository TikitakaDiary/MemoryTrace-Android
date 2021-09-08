package com.upf.memorytrace_android.ui.diary.write.image

import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.LiveEvent

internal class CropViewModel : BaseViewModel() {
    val isFinishCrop = LiveEvent<Unit?>()

    fun onClickBack() {
        navDirections.value = BackDirections()
    }

    fun onClickFinishCrop() {
        isFinishCrop.call()
    }
}