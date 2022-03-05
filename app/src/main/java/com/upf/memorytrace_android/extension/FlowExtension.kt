package com.upf.memorytrace_android.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged


suspend inline fun <T> Flow<T>.distinctCollect(crossinline action: suspend (value: T) -> Unit) {
    distinctUntilChanged().collect(action)
}