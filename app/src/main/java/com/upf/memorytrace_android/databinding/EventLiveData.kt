package com.upf.memorytrace_android.databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias EventLiveData<T> = LiveData<Event<T>>

class MutableEventLiveData<T> : MutableLiveData<Event<T>>() {

    var event: T?
        @Deprecated("getter is NOT supported!", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            if (value != null) {
                setValue(Event(value))
            }
        }

    fun postEvent(value: T?) {
        if (value != null) {
            postValue(Event(value))
        }
    }
}
