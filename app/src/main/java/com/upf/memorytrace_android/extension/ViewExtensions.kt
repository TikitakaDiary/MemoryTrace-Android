package com.upf.memorytrace_android.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

fun TextView.setTextIfNew(text: CharSequence?) {
    if (TextUtils.equals(this.text, text).not()) {
        setText(text)
    }
}

fun ImageView.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(bmp)
    draw(canvas)
    return bmp
}