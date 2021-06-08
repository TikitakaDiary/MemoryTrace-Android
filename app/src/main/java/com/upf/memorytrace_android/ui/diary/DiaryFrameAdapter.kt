package com.upf.memorytrace_android.ui.diary

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseListAdapter

internal class DiaryFrameAdapter : BaseListAdapter<DiaryItem>() {
    override fun getItemViewTypeByItemType(item: DiaryItem) = R.layout.item_diary_frame
}