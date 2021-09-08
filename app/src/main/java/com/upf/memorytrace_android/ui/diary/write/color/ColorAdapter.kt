package com.upf.memorytrace_android.ui.diary.write.color

import androidx.databinding.ViewDataBinding
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter
import com.upf.memorytrace_android.databinding.ItemColorBinding

internal class ColorAdapter : BaseListAdapter<ColorItem>() {
    override fun getItemViewTypeByItemType(item: ColorItem) = R.layout.item_color

    override fun createViewHolder(
        binding: ViewDataBinding,
        viewType: Int
    ) = ColorViewHolder(binding as ItemColorBinding)
}