package com.upf.memorytrace_android.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    const val YYYY_MM_DD_KR = 0
    const val YYYY_M_D_KR = 1
    const val YYYY_MM = 2

    const val FORMAT_yyyy_MM_dd_T_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss"
    const val FORMAT_yyyy_MM_dd_B_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"

    private const val EMPTY = ""

    fun getTodayDate(format: Int): String = getDate(format, Date())

    fun getDate(format: Int, date: Date): String {
        val dateFormat = when (format) {
            YYYY_M_D_KR -> "yyyy년 M월 d일"
            YYYY_MM_DD_KR -> "yyyy년 MM월 dd일"
            YYYY_MM -> "yyyy.MM"
            else -> EMPTY
        }
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }

    fun getYearAndDate(date: Date): String {
        val dateFormat = "yyyy.MM"
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }

    fun getMonth(date: Date): Int {
        return GregorianCalendar().apply { time = date }.get(Calendar.MONTH)
    }

    fun convertStringToDate(format: String, date: String): Date? = SimpleDateFormat(format, Locale.KOREA).parse(date)
}