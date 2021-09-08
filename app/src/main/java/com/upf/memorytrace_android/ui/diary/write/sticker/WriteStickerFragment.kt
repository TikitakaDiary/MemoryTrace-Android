package com.upf.memorytrace_android.ui.diary.write.sticker

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseViewPagerFragment
import com.upf.memorytrace_android.databinding.FragmentWriteStickerBinding
import com.upf.memorytrace_android.ui.diary.write.WriteViewModel

internal class WriteStickerFragment(
    override val viewModel: WriteViewModel,
    @DrawableRes private val stickerList: List<Int>
) : BaseViewPagerFragment<WriteViewModel, FragmentWriteStickerBinding>() {
    override val layoutId = R.layout.fragment_write_sticker
    private val list = stickerList.map { StickerItem(it) }

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