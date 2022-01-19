package com.upf.memorytrace_android.ui.diary.list.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ItemDiaryDateBinding
import com.upf.memorytrace_android.databinding.ItemDiaryLinearBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListItem
import com.upf.memorytrace_android.util.TimeUtil

abstract class DiaryViewHolder(
    parent: ViewGroup,
    @LayoutRes layoutId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
) {
    abstract fun bind(item: DiaryListItem)

    class DiaryDateViewHolder(parent: ViewGroup) :
        DiaryViewHolder(parent, R.layout.item_diary_date) {

        private val binding = ItemDiaryDateBinding.bind(itemView)

        override fun bind(item: DiaryListItem) {
            if (item is DiaryListItem.DateItem) {
                binding.date = TimeUtil.getYearAndDate(item.date)
            }
        }
    }

    class DiaryLinearViewHolder(
        parent: ViewGroup,
        onClick: (position: Int) -> Unit
    ) : DiaryViewHolder(parent, R.layout.item_diary_linear) {

        private val binding = ItemDiaryLinearBinding.bind(itemView).apply {
            root.setOnDebounceClickListener {
                onClick.invoke(adapterPosition)
            }
        }

        override fun bind(item: DiaryListItem) {
            if (item is DiaryListItem.DiaryItem) {
                binding.diary = item
            }
        }
    }
}