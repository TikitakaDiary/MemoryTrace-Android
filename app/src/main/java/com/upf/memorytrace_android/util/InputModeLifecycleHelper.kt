package com.upf.memorytrace_android.util

import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class InputModeLifecycleHelper(
    private var window: Window?
) : LifecycleObserver {

    private var originalMode: Int = SOFT_INPUT_STATE_UNSPECIFIED

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun setAdjustPan() {
        window?.let {
            originalMode = it.attributes?.softInputMode ?: SOFT_INPUT_STATE_UNSPECIFIED
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun restoreOriginalSoftInputMode() {
        if (originalMode != SOFT_INPUT_STATE_UNSPECIFIED) {
            window?.setSoftInputMode(originalMode)
        }
        window = null
    }
}