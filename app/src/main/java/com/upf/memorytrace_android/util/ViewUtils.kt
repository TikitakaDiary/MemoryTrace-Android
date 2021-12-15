package com.upf.memorytrace_android.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R


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

fun showDialog(
    context: Context,
    title: String,
    message: String,
    confirm: String,
    positive: (() -> Unit)? = null
) {
    AlertDialog.Builder(context, R.style.MyDialog)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.cancel), null)
        .setPositiveButton(confirm) { _, _ ->
            positive?.invoke()
        }.create().show()
}


fun showDialog(
    context: Context,
    title: Int,
    message: Int,
    confirm: Int,
    positive: (() -> Unit)? = null
) {
    AlertDialog.Builder(context, R.style.MyDialog)
        .setTitle(context.getString(title))
        .setMessage(context.getString(message))
        .setNegativeButton(context.getString(R.string.cancel), null)
        .setPositiveButton(context.getString(confirm)) { dialog: DialogInterface, _ ->
            dialog.dismiss()
            positive?.invoke()
        }.create().show()
}

//중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
private val MIN_CLICK_INTERVAL = 300
private var mLastClickTime: Long = 0

fun isSingleClick(): Boolean {
    val currentClickTime = SystemClock.uptimeMillis()
    val elapsedTime = currentClickTime - mLastClickTime
    mLastClickTime = currentClickTime
    // 중복클릭 아닌 경우
    if (elapsedTime > MIN_CLICK_INTERVAL) {
        return true
    }
    return false
}