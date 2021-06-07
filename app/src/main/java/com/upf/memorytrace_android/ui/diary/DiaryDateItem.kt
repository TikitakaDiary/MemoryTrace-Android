package com.upf.memorytrace_android.ui.diary

import com.upf.memorytrace_android.base.BaseItem
import com.upf.memorytrace_android.util.TimeUtil
import java.util.Date

data class DiaryDateItem(
    val date: Date,
    override val itemId: String = date.toString()
) : BaseItem {
    val dateStr: String
        get() = TimeUtil.getDate(TimeUtil.YYYY_MM, date)
}