package com.upf.memorytrace_android.ui.diary.write.sticker

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetWriteStickerBinding
import com.upf.memorytrace_android.util.StickerPackage
import com.upf.memorytrace_android.ui.diary.write.WriteViewModel

internal class WriteStickerBottomSheetFragment(
    override val viewModel: WriteViewModel
) : BaseBottomSheetFragment<WriteViewModel, FragmentBottomSheetWriteStickerBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_write_sticker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
    }

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
            tab.setIcon(StickerPackage.packageMap.keyAt(pos))
        }.attach()
    }
}