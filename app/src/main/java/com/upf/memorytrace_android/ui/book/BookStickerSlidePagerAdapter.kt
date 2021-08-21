package com.upf.memorytrace_android.ui.book

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.upf.memorytrace_android.util.StickerPackage
import com.upf.memorytrace_android.viewmodel.CreateBookViewModel

internal class BookStickerSlidePagerAdapter(
    fa: FragmentActivity,
    private val viewModel: CreateBookViewModel
) : FragmentStateAdapter(fa) {
    override fun getItemCount() = StickerPackage.packageMap.size
    override fun createFragment(position: Int) = BookStickerFragment(viewModel, StickerPackage.packageMap.valueAt(position) ?: listOf())
}