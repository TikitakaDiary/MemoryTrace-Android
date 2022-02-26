package com.upf.memorytrace_android.extension

import android.view.View
import androidx.core.view.isVisible
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.util.OnDebounceClickListener
import java.lang.Long.max

fun View.setOnDebounceClickListener(
    interval: Long = OnDebounceClickListener.DEFAULT_INTERVAL,
    listener: View.OnClickListener
) {
    setOnClickListener(
        OnDebounceClickListener(max(0, interval), listener)
    )
}

fun View.showIfDebug() {
    isVisible = BuildConfig.DEBUG
}