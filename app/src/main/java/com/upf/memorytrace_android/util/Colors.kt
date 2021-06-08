package com.upf.memorytrace_android.util

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.upf.memorytrace_android.R

enum class Colors(val id: Int, val color: Long) {

    RED(1, 0xffD54E4E),
    ORANGE(2, 0xffF59728),
    YELLOW(3, 0xffFFD84C),
    GREEN(4, 0xff68C459),
    OCEAN_BLUE(5, 0xff43CAC2),
    BLUE(6, 0xff3578DC),
    DARK_BLUE(7, 0xff2A43C7),
    PURPLE(8, 0xff8B47D0),
    PINK(9, 0xffEA7AA6),
    LIGHT_GRAY(10, 0xffB1B1B1),
    DARK_GRAY(11, 0xff696969),
    SYSTEM_GRAY(0, 0xff4f4f4f);

    fun toInt() = color.toInt()


    companion object {
        fun getColorHex(id: Int): String {
            for (current in values()) {
                if (current.id == id)
                    return "#${Integer.toHexString(current.color.toInt()).substring(2)}"

            }
            return ""
        }

        fun getColor(id: Int): Colors {
            for (current in values()) {
                if (current.id == id) return current
            }
            return RED
        }

        fun getColors(): List<Colors> {
            return mutableListOf(
                RED,
                ORANGE,
                YELLOW,
                GREEN,
                OCEAN_BLUE,
                BLUE,
                DARK_BLUE,
                PURPLE,
                PINK,
                LIGHT_GRAY,
                DARK_GRAY,
                SYSTEM_GRAY
            )
        }

        fun fillCircle(view: View?, color: Colors) {
            view?.apply {
                background = ContextCompat.getDrawable(context, R.drawable.ic_circle)
                (background as? GradientDrawable)?.setColor(color.toInt())
            }
        }
    }
}