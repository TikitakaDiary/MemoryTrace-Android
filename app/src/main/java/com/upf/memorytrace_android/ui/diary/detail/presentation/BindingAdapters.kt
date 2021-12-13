package com.upf.memorytrace_android.ui.diary.detail.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.upf.memorytrace_android.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("editButtonColor")
    fun TextView.setEditButtonColor(isModifiable: Boolean) {
        if (isModifiable) {
            setTextColor(context.getColor(R.color.systemWhite))
        } else {
            setTextColor(context.getColor(R.color.gray69))
        }
    }
}