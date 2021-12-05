package com.upf.memorytrace_android.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.util.OnDebounceClickListener

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("isVisible")
    fun ProgressBar.isVisible(uiState: UiState<*>) {
        this.isVisible = uiState is UiState.Loading
    }

    @JvmStatic
    @BindingAdapter("showToast")
    fun View.showToast(uiState: UiState<*>) {
        if (uiState is UiState.Failure) {
            uiState.throwable?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun View.isVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }

    @JvmStatic
    @BindingAdapter("enabled")
    fun View.setEnabled(content: String?) {
        isEnabled = !content.isNullOrEmpty()
    }

    @JvmStatic
    @BindingAdapter("onDebounceClick")
    fun View.setOnDebounceClickListener(listener: View.OnClickListener) {
        setOnDebounceClickListener(OnDebounceClickListener.DEFAULT_INTERVAL, listener)
    }
}