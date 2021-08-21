package com.upf.memorytrace_android.ui.write

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.upf.memorytrace_android.util.StickerPackage
import com.upf.memorytrace_android.viewmodel.WriteViewModel

internal class StickerSlidePagerAdapter(
    fa: FragmentActivity,
    private val viewModel: WriteViewModel
) : FragmentStateAdapter(fa) {
    override fun getItemCount() = StickerPackage.packageMap.size
    override fun createFragment(position: Int) =
        WriteStickerFragment(viewModel, StickerPackage.packageMap.valueAt(position) ?: listOf())
}