package com.upf.memorytrace_android.ui.diary.list

import com.upf.memorytrace_android.ui.base.BaseViewHolder
import com.upf.memorytrace_android.databinding.ItemDiaryGridListBinding
import com.upf.memorytrace_android.ui.diary.list.item.DiaryMonthItem
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListViewModel

internal class DiaryGridListViewHolder(
    binding: ItemDiaryGridListBinding,
    listViewModel: DiaryListViewModel
) : BaseViewHolder<DiaryMonthItem>(binding) {
    private val gridAdapter = DiaryGridAdapter()

    init {
//        gridAdapter.setViewHolderViewModel(listViewModel)
        binding.gridRv.adapter = gridAdapter
    }

    override fun bind(item: DiaryMonthItem) {
        super.bind(item)
        gridAdapter.submitList(item.diaryList)
    }
}