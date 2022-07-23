package com.upf.memorytrace_android.util

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.upf.memorytrace_android.R

enum class Colors(val id: Int, val color: Long) {

    NONE(-1, 0x00FFFFFF),
    RED(0, 0xffD54E4E),
    ORANGE(1, 0xffF59728),
    YELLOW(2, 0xffFFD84C),
    GREEN(3, 0xff68C459),
    OCEAN_BLUE(4, 0xff43CAC2),
    BLUE(5, 0xff3578DC),
    DARK_BLUE(6, 0xff2A43C7),
    PURPLE(7, 0xff8B47D0),
    PINK(8, 0xffEA7AA6),
    LIGHT_GRAY(9, 0xffB1B1B1),
    DARK_GRAY(10, 0xff696969),
    SYSTEM_GRAY(11, 0xff4f4f4f);

    fun toInt() = color.toInt()


    companion object {

        fun getColorToHex(color: Colors): String {
            return "#${Integer.toHexString(color.toInt()).substring(2)}"
        }

        fun getColorToHex(color: Int): String {
            for (current in values()) {
                if (current.id == color)
                    return "#${Integer.toHexString(current.color.toInt()).substring(2)}"
            }
            return ""
        }

        fun getColor(id: Int): Colors {
            for (current in values()) {
                if (current.id == id)
                    return current
            }
            return SYSTEM_GRAY
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

        fun fillColor(view: View?, color: Colors) {
            view?.apply {
                setBackgroundColor(color.toInt())
            }
        }

        fun fillColorTint(view: View?, color: Colors) {
            view?.apply {
                background.setTint(Color.parseColor(getColorToHex(color)))
            }
        }

        fun fillColorTint(view: View?, color: Int) {
            view?.apply {
                background.setTint(Color.parseColor(getColorToHex(color)))
            }
        }

        fun fillCircle(view: View?, color: Colors) {
            view?.apply {
                background = ContextCompat.getDrawable(context, R.drawable.ic_circle)
                (background as? GradientDrawable)?.setColor(color.toInt())
            }
        }
    }
}