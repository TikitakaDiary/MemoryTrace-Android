package com.upf.memorytrace_android.color

import android.graphics.Color
import androidx.annotation.ColorInt

enum class UserColor(
    val hexCode: String
) {
    NONE("#00FFFFFF"),
    RED("#ffD54E4E"),
    ORANGE("#ffF59728"),
    YELLOW("#ffFFD84C"),
    GREEN("#ff68C459"),
    OCEAN_BLUE("#ff43CAC2"),
    BLUE("#ff3578DC"),
    DARK_BLUE("#ff2A43C7"),
    PURPLE("#ff8B47D0"),
    PINK("#ffEA7AA6"),
    LIGHT_GRAY("#ffB1B1B1"),
    DARK_GRAY("#ff696969"),
    SYSTEM_GRAY("#ff4f4f4f");

    companion object {
        fun getAllColors(): List<UserColor> {
            return values().filter { it != NONE }
        }
    }
}

@ColorInt
fun UserColor.toColorInt(): Int {
    return Color.parseColor(hexCode)
}