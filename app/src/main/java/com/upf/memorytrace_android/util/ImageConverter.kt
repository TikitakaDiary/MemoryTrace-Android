package com.upf.memorytrace_android.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

internal object ImageConverter {

    fun convertViewToBitmap(v: View): Bitmap {
        val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bmp)
        v.draw(canvas)
        return bmp
    }
}