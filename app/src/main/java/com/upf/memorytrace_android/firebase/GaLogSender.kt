package com.upf.memorytrace_android.firebase

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object GaLogSender {

    const val GA_EVENT_SHOW_SPONSOR_POPUP = "show_sponsor_popup"

    fun sendEvent(eventName: String, param: Bundle? = null) {
        Firebase.analytics.logEvent(eventName, param)
        Log.d(javaClass.simpleName, "sendEvent: eventName : $eventName, param : $param")
    }
}