package com.upf.memorytrace_android.extension

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.EventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ComponentActivity.repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch { lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block) }
}

fun <T> ComponentActivity.observeEvent(
    event: EventLiveData<T>,
    onEventUnhandledContent: (T) -> Unit
) {
    event.observe(this, EventObserver(onEventUnhandledContent))
}

fun Activity.clearFocusAndHideSoftInput() {
    val focusedView = currentFocus
    focusedView?.clearFocus()
    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focusedView?.windowToken, 0)
}