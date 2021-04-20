package com.upf.memorytrace_android.api.util

import com.upf.memorytrace_android.MemoryTraceApplication
import com.upf.memorytrace_android.api.MemoryTraceService

object MemoryTraceUtils {
    fun apiService(): MemoryTraceService = MemoryTraceApplication().appComponent.getApi()
}