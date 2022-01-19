package com.upf.memorytrace_android.ui.diary.list

import com.upf.memorytrace_android.ui.base.BaseViewHolder
import com.upf.memorytrace_android.databinding.ItemDiaryFrameListBinding
import com.upf.memorytrace_android.ui.diary.list.item.DiaryMonthItem
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListViewModel

internal class DiaryFrameListViewHolder(
    binding: ItemDiaryFrameListBinding,
    listViewModel: DiaryListViewModel
) : BaseViewHolder<DiaryMonthItem>(binding) {
    private val frameAdapter = DiaryFrameAdapter()

    init {
//        frameAdapter.setViewHolderViewModel(listViewModel)
        binding.frameRv.adapter = frameAdapter
    }

    override fun bind(item: DiaryMonthItem) {
        super.bind(item)
        frameAdapter.submitList(item.diaryList)
    }
}