package com.upf.memorytrace_android.ui.diary.list.presentation.adapter

import android.view.ViewGroup
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListItem
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.DiaryViewHolder

class DiaryLinearAdapter: DiaryAdapter<DiaryViewHolder.DiaryLinearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): DiaryViewHolder.DiaryLinearViewHolder {
        return DiaryViewHolder.DiaryLinearViewHolder(parent) { position ->
            val item = getItem(position)
            if (item is DiaryListItem.DiaryItem) {
                item.onItemClick.invoke(item.diaryId)
            }
        }
    }
}