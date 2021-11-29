package com.upf.memorytrace_android.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("isVisible")
    fun ProgressBar.isVisible(uiState: UiState) {
        this.isVisible = uiState is UiState.Loading
    }

    @JvmStatic
    @BindingAdapter("showToast")
    fun View.showToast(uiState: UiState) {
        if (uiState is UiState.Failure) {
            uiState.throwable?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}