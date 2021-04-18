package com.upf.memorytrace_android.extension

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Context.toast(msg: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(msg), duration)
}

internal fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

internal fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(msg, duration)
}