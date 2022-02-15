package com.upf.memorytrace_android.ui.diary.list.presentation.adapter

import android.view.ViewGroup
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListItem
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.DiaryViewHolder

class DiaryGridAdapter: DiaryAdapter<DiaryViewHolder.DiaryGridViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): DiaryViewHolder.DiaryGridViewHolder {
        return DiaryViewHolder.DiaryGridViewHolder(parent) { position ->
            val item = getItem(position)
            if (item is DiaryListItem.DiaryItem) {
                item.onItemClick.invoke(item.diaryId)
            }
        }
    }
}