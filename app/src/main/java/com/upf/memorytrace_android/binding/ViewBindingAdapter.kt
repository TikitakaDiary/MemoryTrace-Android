package com.upf.memorytrace_android.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
internal fun setSrcWithBitmap(iv: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        iv.setImageBitmap(it)
    }
}