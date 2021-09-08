package com.upf.memorytrace_android.ui.diary.list

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter
import com.upf.memorytrace_android.ui.diary.list.item.DiaryItem

internal class DiaryFrameAdapter : BaseListAdapter<DiaryItem>() {
    override fun getItemViewTypeByItemType(item: DiaryItem) = R.layout.item_diary_frame
}