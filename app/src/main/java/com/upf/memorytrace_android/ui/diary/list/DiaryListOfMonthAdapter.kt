package com.upf.memorytrace_android.ui.diary.list

import androidx.databinding.ViewDataBinding
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter
import com.upf.memorytrace_android.databinding.ItemDiaryFrameListBinding
import com.upf.memorytrace_android.databinding.ItemDiaryGridListBinding
import com.upf.memorytrace_android.ui.diary.list.item.DiaryMonthItem

internal class DiaryListOfMonthAdapter : BaseListAdapter<DiaryMonthItem>() {
    private var listType = DiaryListType.GRID

    override fun getItemViewTypeByItemType(item: DiaryMonthItem) = when (listType) {
        DiaryListType.FRAME -> R.layout.item_diary_frame_list
        DiaryListType.GRID -> R.layout.item_diary_grid_list
    }

    override fun createViewHolder(
        binding: ViewDataBinding,
        viewType: Int
    ) = when (listType) {
        DiaryListType.FRAME -> DiaryFrameListViewHolder(
            binding as ItemDiaryFrameListBinding,
            viewModel as DiaryViewModel
        )
        DiaryListType.GRID -> DiaryGridListViewHolder(
            binding as ItemDiaryGridListBinding,
            viewModel as DiaryViewModel
        )
    }

    fun changeListType(type: DiaryListType, list: List<DiaryMonthItem>?) {
        listType = type
        list?.let { submitList(it) }
        notifyDataSetChanged()
    }
}