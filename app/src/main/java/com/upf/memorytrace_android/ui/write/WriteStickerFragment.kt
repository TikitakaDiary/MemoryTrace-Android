package com.upf.memorytrace_android.ui.write

import android.os.Bundle
import android.view.View
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseViewPagerFragment
import com.upf.memorytrace_android.databinding.FragmentWriteStickerBinding
import com.upf.memorytrace_android.viewmodel.WriteViewModel

internal class WriteStickerFragment(
    override val viewModel: WriteViewModel
) : BaseViewPagerFragment<WriteViewModel, FragmentWriteStickerBinding>() {
    override val layoutId = R.layout.fragment_write_sticker

    private val list = listOf(
        StickerItem(R.drawable.ic_grid),
        StickerItem(R.drawable.ic_frame),
        StickerItem(R.drawable.ic_album),
        StickerItem(R.drawable.ic_add_diary),
        StickerItem(R.drawable.ic_camera)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stickerRv.run {
            adapter = StickerAdapter().apply {
                submitList(list)
                setViewHolderViewModel(this@WriteStickerFragment.viewModel)
            }
        }
    }
}