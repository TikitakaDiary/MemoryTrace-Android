package com.upf.memorytrace_android.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


object ViewUtils {

    private var lastClickTime: Long = 0
    private val CLICK_INTERVAL: Long = 700 //중복클릭 판단 간격

    val isSimpleClick: Boolean
        get() = isSimpleClick(CLICK_INTERVAL)

    private fun isSimpleClick(interval: Long): Boolean {
        val now = System.currentTimeMillis()
        if (Math.abs(now - lastClickTime) < interval) {
            // 빠른 시간내(CLICK_INTERVAL)에 들어오는 클릭이벤트 무시
            return false
        }
        lastClickTime = now
        // 클릭 이벤트 처리
        return true
    }

}

fun View.setOnSimpleClickListener(listener: View.OnClickListener) {
    this.setOnClickListener {
        if (ViewUtils.isSimpleClick) {
            listener.onClick(this)
        }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.activate() {
    isActivated = true
}

fun View.activateIfNot() {
    if (!isActivated) {
        isActivated = true
    }
}

fun View.deactivate() {
    isActivated = false
}

fun View.deactivateIfNot() {
    if (isActivated) {
        isActivated = false
    }
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


fun View.setOnSimpleClickListener(listener: (View) -> Unit) {
    this.setOnClickListener {
        if (ViewUtils.isSimpleClick) {
            listener.invoke(this)
        }
    }
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
