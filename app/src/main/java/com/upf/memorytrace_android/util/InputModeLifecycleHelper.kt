package com.upf.memorytrace_android.util

import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

fun Window?.getSoftInputMode(): Int {
    return this?.attributes?.softInputMode ?: SOFT_INPUT_STATE_UNSPECIFIED
}

class InputModeLifecycleHelper(
    private var window: Window?,
    private val mode: Mode = Mode.ADJUST_RESIZE
) : LifecycleObserver {

    private var originalMode: Int = SOFT_INPUT_STATE_UNSPECIFIED

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun setNewSoftInputMode() {
        window?.let {
            originalMode = it.getSoftInputMode()

            when (mode) {
                Mode.ADJUST_RESIZE -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        window?.setDecorFitsSystemWindows(false)
                        window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                            val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                            window?.decorView?.setPadding(0, 0, 0, imeHeight)
                            insets
                        }
                    } else {
                        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    }
                }
                Mode.ADJUST_PAN -> it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun restoreOriginalSoftInputMode() {
        if (originalMode != SOFT_INPUT_STATE_UNSPECIFIED) {
            window?.setSoftInputMode(originalMode)
        }
        window = null
    }

    enum class Mode {
        ADJUST_RESIZE, ADJUST_PAN
    }
}