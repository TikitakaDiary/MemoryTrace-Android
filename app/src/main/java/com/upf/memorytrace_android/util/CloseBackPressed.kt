package com.upf.memorytrace_android.util

import android.content.Context
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.extension.toast

private const val FINISH_INTERVAL_TIME: Long = 2000
private var backPressedTime: Long = 0

fun closeBackPressed(context: Context): Boolean {
    val tempTime = System.currentTimeMillis()
    val intervalTime = tempTime - backPressedTime
    return if (intervalTime in 0..FINISH_INTERVAL_TIME) {
        true
    } else {
        backPressedTime = tempTime
        context.toast(R.string.close_back_pressed)
        false
    }
}