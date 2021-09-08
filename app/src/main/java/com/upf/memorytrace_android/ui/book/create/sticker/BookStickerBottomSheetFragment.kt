package com.upf.memorytrace_android.ui.book.create.sticker

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetBookStickerBinding
import com.upf.memorytrace_android.ui.book.create.CreateBookViewModel
import com.upf.memorytrace_android.util.StickerPackage

internal class BookStickerBottomSheetFragment(
    override val viewModel: CreateBookViewModel
) : BaseBottomSheetFragment<CreateBookViewModel, FragmentBottomSheetBookStickerBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_book_sticker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewPager()
    }

    private fun initializeViewPager() {
        binding.stickerViewpager.adapter = BookStickerSlidePagerAdapter(
            requireActivity(),
            viewModel
        )
        TabLayoutMediator(binding.stickerTabLayout, binding.stickerViewpager) { tab, pos ->
            tab.setIcon(StickerPackage.packageMap.keyAt(pos))
        }.attach()
    }
}