package com.upf.memorytrace_android.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun RecyclerView.topScroll() {
    if (this.layoutManager is LinearLayoutManager) {
        (this.layoutManager as LinearLayoutManager).run {

            val scroller = object : LinearSmoothScroller(this@topScroll.context) {
                override fun getVerticalSnapPreference(): Int {
                    return LinearSmoothScroller.SNAP_TO_START
                }
            }

            scroller.targetPosition = 0

            this.startSmoothScroll(scroller)

        }
    }
}

fun RecyclerView.isScrolled(checkerFirstVisibleItemPosition: Int = 2): Boolean {
    if (this.scrollState == RecyclerView.SCROLL_STATE_IDLE
        && this.layoutManager is LinearLayoutManager
    ) {
        (this.layoutManager as LinearLayoutManager).run {
            if (findFirstVisibleItemPosition() > checkerFirstVisibleItemPosition) {
                return true
            }
        }
    }
    return false
}
