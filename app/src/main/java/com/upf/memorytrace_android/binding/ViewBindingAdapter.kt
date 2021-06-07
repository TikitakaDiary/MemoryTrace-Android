package com.upf.memorytrace_android.binding

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:src")
internal fun setSrcWithBitmap(iv: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        iv.setImageBitmap(it)
    }
}

@BindingAdapter("android:src")
internal fun setSrcWithUrl(iv: ImageView, url: String?) {
    url?.let {
        Glide.with(iv.context)
            .load(it)
            .into(iv)
    }
}

@BindingAdapter("android:src")
internal fun setSrcWithDrawableRes(iv: ImageView, @DrawableRes resId: Int?) {
    resId?.let {
        iv.setImageResource(it)
    }
}

@BindingAdapter("android:visibility")
internal fun setVisibility(v: View, isVisible: Boolean) {
    v.visibility = when (isVisible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}