package com.upf.memorytrace_android.ui.diary

import com.upf.memorytrace_android.base.BaseViewHolder
import com.upf.memorytrace_android.databinding.ItemDiaryGridListBinding
import com.upf.memorytrace_android.viewmodel.DiaryViewModel

internal class DiaryGridListViewHolder(
    binding: ItemDiaryGridListBinding,
    viewModel: DiaryViewModel
) : BaseViewHolder<DiaryMonthItem>(binding) {
    private val gridAdapter = DiaryGridAdapter()

    init {
        gridAdapter.setViewHolderViewModel(viewModel)
        binding.gridRv.adapter = gridAdapter
    }

    override fun bind(item: DiaryMonthItem) {
        super.bind(item)
        gridAdapter.submitList(item.diaryList)
    }
}