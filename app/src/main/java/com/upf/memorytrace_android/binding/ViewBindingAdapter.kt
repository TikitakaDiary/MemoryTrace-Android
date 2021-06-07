package com.upf.memorytrace_android.binding

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
internal fun setSrcWithBitmap(iv: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        iv.setImageBitmap(it)
    }
}

@BindingAdapter("android:visibility")
internal fun setVisibility(v: View, isVisible: Boolean) {
    v.visibility = when (isVisible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}