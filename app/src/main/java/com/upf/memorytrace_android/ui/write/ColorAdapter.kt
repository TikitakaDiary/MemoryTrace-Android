package com.upf.memorytrace_android.ui.write

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseListAdapter

internal class ColorAdapter : BaseListAdapter<ColorItem>() {
    override fun getItemViewTypeByItemType(item: ColorItem) = R.layout.item_color
}