package com.upf.memorytrace_android.ui.diary

import com.upf.memorytrace_android.base.BaseViewHolder
import com.upf.memorytrace_android.databinding.ItemDiaryFrameListBinding
import com.upf.memorytrace_android.viewmodel.DiaryViewModel

internal class DiaryFrameListViewHolder(
    binding: ItemDiaryFrameListBinding,
    viewModel: DiaryViewModel
) : BaseViewHolder<DiaryMonthItem>(binding) {
    private val frameAdapter = DiaryFrameAdapter()

    init {
        frameAdapter.setViewHolderViewModel(viewModel)
        binding.frameRv.adapter = frameAdapter
    }

    override fun bind(item: DiaryMonthItem) {
        super.bind(item)
        frameAdapter.submitList(item.diaryList)
    }
}