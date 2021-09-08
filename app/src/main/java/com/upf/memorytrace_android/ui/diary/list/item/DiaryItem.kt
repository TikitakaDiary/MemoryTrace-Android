package com.upf.memorytrace_android.ui.diary.list.item

import com.upf.memorytrace_android.ui.base.BaseItem
import com.upf.memorytrace_android.util.TimeUtil
import java.util.Date

data class DiaryItem(
    val id: Int,
    val title: String,
    val nickname: String,
    val img: String,
    val date: Date,
    override val itemId: String = id.toString()
) : BaseItem {
    val dateStr: String
        get() = TimeUtil.getDate(TimeUtil.YYYY_M_D_KR, date)
}