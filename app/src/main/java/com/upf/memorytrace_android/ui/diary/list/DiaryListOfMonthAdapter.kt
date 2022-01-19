package com.upf.memorytrace_android.ui.diary.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.upf.memorytrace_android.BR
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ItemDiaryFrameListBinding
import com.upf.memorytrace_android.databinding.ItemDiaryGridListBinding
import com.upf.memorytrace_android.ui.base.BaseViewHolder
import com.upf.memorytrace_android.ui.diary.list.item.DiaryMonthItem
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryListViewModel

internal class DiaryListOfMonthAdapter
    : ListAdapter<DiaryMonthItem, BaseViewHolder<DiaryMonthItem>>(diffUtil) {

    private var listType = DiaryListType.GRID
    var listViewModel: DiaryListViewModel? = null

    fun changeListType(type: DiaryListType, list: List<DiaryMonthItem>?) {
        listType = type
        list?.let { submitList(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DiaryMonthItem> {
        val inflate = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflate, viewType, parent, false)
        val viewHolder = createViewHolder(binding)
        binding.setVariable(BR.viewModel, listViewModel)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int = when (listType) {
        DiaryListType.FRAME -> R.layout.item_diary_frame_list
        DiaryListType.GRID -> R.layout.item_diary_grid_list
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DiaryMonthItem>, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createViewHolder(
        binding: ViewDataBinding
    ) = when (listType) {
        DiaryListType.FRAME -> DiaryFrameListViewHolder(
            binding as ItemDiaryFrameListBinding,
            listViewModel as DiaryListViewModel
        )
        DiaryListType.GRID -> DiaryGridListViewHolder(
            binding as ItemDiaryGridListBinding,
            listViewModel as DiaryListViewModel
        )
    }

    fun setViewHolderViewModel(vm: DiaryListViewModel) {
        listViewModel = vm
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<DiaryMonthItem>() {
            override fun areItemsTheSame(
                oldItem: DiaryMonthItem,
                newItem: DiaryMonthItem
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DiaryMonthItem,
                newItem: DiaryMonthItem
            ): Boolean = oldItem.hashCode() == newItem.hashCode()
        }
    }
}