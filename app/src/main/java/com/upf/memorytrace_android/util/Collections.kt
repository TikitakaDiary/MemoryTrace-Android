package com.upf.memorytrace_android.util

fun <T> MutableList<T>.clearAndAddAll(elements: Collection<T>?) {
    clear()
    elements?.let { addAll(it) }
}