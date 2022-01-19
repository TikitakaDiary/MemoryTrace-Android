package com.upf.memorytrace_android.extension

import android.util.Log
import androidx.lifecycle.ViewModel

fun ViewModel.logError(msg: String?) {
    msg?.let {
        Log.e(javaClass.simpleName, it)
    }
}

fun ViewModel.logError(tr: Throwable?) {
    tr.let {
        Log.e(javaClass.simpleName, "", it)
    }
}