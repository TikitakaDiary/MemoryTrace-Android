package com.upf.memorytrace_android.api.util

import com.upf.memorytrace_android.MemoryTraceApplication
import com.upf.memorytrace_android.api.MemoryTraceServiceOld

object MemoryTraceUtils {
    fun apiService(): MemoryTraceServiceOld = MemoryTraceApplication().appComponent.getApi()
}