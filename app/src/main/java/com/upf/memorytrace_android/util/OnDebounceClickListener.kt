package com.upf.memorytrace_android.util

import android.view.View

class OnDebounceClickListener(
    private val interval: Long = DEFAULT_INTERVAL,
    private val listener: View.OnClickListener?
) : View.OnClickListener {

    companion object {
        const val DEFAULT_INTERVAL = 300L
    }

    private var lastClickedMillis: Long = 0

    override fun onClick(view: View?) {
        listener?.run {
            val now = System.currentTimeMillis()
            if (now - lastClickedMillis < interval) {
                return@run
            }
            lastClickedMillis = now
            onClick(view)
        }
    }
}