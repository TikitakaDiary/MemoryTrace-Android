package com.upf.memorytrace_android.util

import android.view.animation.Animation

fun Animation.setAnimationListener(
    onAnimationStart: ((Animation?) -> Unit)? = null,
    onAnimationEnd: ((Animation?) -> Unit)? = null,
    onAnimationRepeat: ((Animation?) -> Unit)? = null
) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onAnimationStart?.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onAnimationEnd?.invoke(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            onAnimationRepeat?.invoke(animation)
        }
    })
}