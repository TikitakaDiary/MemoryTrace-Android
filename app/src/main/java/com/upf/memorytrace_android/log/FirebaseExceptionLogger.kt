package com.upf.memorytrace_android.log

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseExceptionLogger : ExceptionLogger {
    override fun logException(e: Throwable) {
        FirebaseCrashlytics.getInstance().run {
            e.message?.let { log(it) }
            recordException(e)
        }
    }
}