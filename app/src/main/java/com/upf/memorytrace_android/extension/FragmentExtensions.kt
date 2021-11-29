package com.upf.memorytrace_android.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.EventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun <T> Fragment.observeEvent(event: EventLiveData<T>, onEventUnhandledContent: (T) -> Unit) {
    event.observe(viewLifecycleOwner, EventObserver(onEventUnhandledContent))
}