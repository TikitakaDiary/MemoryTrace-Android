package com.upf.memorytrace_android.ui.write

import com.upf.memorytrace_android.base.BaseItem
import com.upf.memorytrace_android.util.Colors

internal class ColorItem(
    val color: Colors,
    override val itemId: String = color.id.toString()
) : BaseItem