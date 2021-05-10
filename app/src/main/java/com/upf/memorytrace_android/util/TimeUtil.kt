package com.upf.memorytrace_android.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    const val YYYY_MM_DD_KR = 0
    const val YYYY_M_D_KR = 1

    private const val EMPTY = ""

    fun getTodayDate(format: Int): String = getDate(format, Date())

    fun getDate(format: Int, date: Date): String {
        val dateFormat = when (format) {
            YYYY_M_D_KR -> "yyyy년 M월 d일"
            YYYY_MM_DD_KR -> "yyyy년 MM월 dd일"
            else -> EMPTY
        }
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }
}