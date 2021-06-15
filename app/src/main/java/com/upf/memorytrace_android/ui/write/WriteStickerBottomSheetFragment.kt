package com.upf.memorytrace_android.ui.write

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetWriteStickerBinding
import com.upf.memorytrace_android.viewmodel.WriteViewModel

internal class WriteStickerBottomSheetFragment(
    override val viewModel: WriteViewModel
) : BaseBottomSheetFragment<WriteViewModel, FragmentBottomSheetWriteStickerBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_write_sticker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewPager()
    }

    private fun initializeViewPager() {
        binding.stickerViewpager.adapter = StickerSlidePagerAdapter(
            requireActivity(),
            viewModel
        )
        TabLayoutMediator(binding.stickerTabLayout, binding.stickerViewpager) { tab, pos ->

        }.attach()
    }
}