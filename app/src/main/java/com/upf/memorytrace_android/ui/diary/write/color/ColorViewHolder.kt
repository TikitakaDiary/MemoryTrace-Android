package com.upf.memorytrace_android.ui.diary.write.color

import com.upf.memorytrace_android.ui.base.BaseViewHolder
import com.upf.memorytrace_android.databinding.ItemColorBinding
import com.upf.memorytrace_android.util.Colors

internal class ColorViewHolder(
    private val binding: ItemColorBinding
) : BaseViewHolder<ColorItem>(binding) {

    override fun bind(item: ColorItem) {
        super.bind(item)
        Colors.fillCircle(binding.circleColor, item.color)
    }
}