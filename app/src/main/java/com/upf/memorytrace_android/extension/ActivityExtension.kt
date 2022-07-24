package com.upf.memorytrace_android.extension

import android.app.Activity
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
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

fun FragmentActivity?.showAllowingStateLoss(
    tag: String?,
    dialogFragmentFactory: () -> DialogFragment
) {
    if (this == null) return
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(dialogFragmentFactory(), tag)
    transaction.commitAllowingStateLoss()
}

fun Activity.isGrantedPermission(permission: String): Boolean {
    return isGrantedPermission(listOf(permission))
}

fun Activity.isGrantedPermission(permissions: List<String>): Boolean {
    permissions.forEach { permission ->
        val permissionState = ActivityCompat.checkSelfPermission(this, permission)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            return false
        }
    }
    return true
}

fun Activity.requestPermission(permission: String, requestCode: Int) {
    requestPermissions(arrayOf(permission), requestCode)
}