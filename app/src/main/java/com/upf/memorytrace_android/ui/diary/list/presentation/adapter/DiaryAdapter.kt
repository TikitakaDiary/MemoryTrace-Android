package com.upf.memorytrace_android.ui.diary.list.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListItem
import com.upf.memorytrace_android.ui.diary.list.presentation.areContentsTheSame
import com.upf.memorytrace_android.ui.diary.list.presentation.areItemsTheSame
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.DiaryViewHolder

abstract class DiaryAdapter<VH : DiaryViewHolder>
    : ListAdapter<DiaryListItem, DiaryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        return if (viewType == VIEW_TYPE_DATE) {
            DiaryViewHolder.DiaryDateViewHolder(parent)
        } else {
            onCreateViewHolder(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DiaryListItem.DiaryItem -> VIEW_TYPE_ITEM
            is DiaryListItem.DateItem -> VIEW_TYPE_DATE
        }
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    companion object {
        const val VIEW_TYPE_DATE = 0
        const val VIEW_TYPE_ITEM = 1

        private val diffUtil = object : DiffUtil.ItemCallback<DiaryListItem>() {

            override fun areItemsTheSame(
                oldItem: DiaryListItem,
                newItem: DiaryListItem
            ): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(
                oldItem: DiaryListItem,
                newItem: DiaryListItem
            ): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }
        }
    }
}