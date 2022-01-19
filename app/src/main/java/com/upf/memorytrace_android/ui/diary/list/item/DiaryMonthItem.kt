package com.upf.memorytrace_android.ui.diary.list.item

import com.upf.memorytrace_android.ui.base.BaseItem

data class DiaryMonthItem(
    val date: String,
    val diaryList: List<DiaryItem>,
    override val itemId: String = date
) : BaseItem