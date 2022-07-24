package com.upf.memorytrace_android.extension

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.EventObserver
import com.upf.memorytrace_android.util.InputModeLifecycleHelper
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

fun Fragment.applyAdjustPanMode() {
    viewLifecycleOwner.lifecycle.addObserver(
        InputModeLifecycleHelper(
            activity?.window
        )
    )
}

fun Fragment.showKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun FragmentManager?.showAllowingStateLoss(
    tag: String?,
    dialogFragmentFactory: () -> DialogFragment
) {
    if (this == null) return
    val transaction = beginTransaction()
    transaction.add(dialogFragmentFactory(), tag)
    transaction.commitAllowingStateLoss()
}

fun Fragment.isGrantedPermission(permissions: List<String>): Boolean {
    val parentActivity = activity
    if (parentActivity == null) {
        Log.e(javaClass.simpleName, "parent activity is null")
        return false
    }
    return parentActivity.isGrantedPermission(permissions)
}