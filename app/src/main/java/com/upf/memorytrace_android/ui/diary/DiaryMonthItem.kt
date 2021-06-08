package com.upf.memorytrace_android.ui.diary

import com.upf.memorytrace_android.base.BaseItem

internal data class DiaryMonthItem(
    val date: String,
    val diaryList: List<DiaryItem>,
    override val itemId: String = date
) : BaseItem