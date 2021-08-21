package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseViewPagerFragment
import com.upf.memorytrace_android.databinding.FragmentBookStickerBinding
import com.upf.memorytrace_android.ui.write.StickerItem
import com.upf.memorytrace_android.viewmodel.CreateBookViewModel

internal class BookStickerFragment(
    override val viewModel: CreateBookViewModel,
    @DrawableRes private val stickerList: List<Int>
) : BaseViewPagerFragment<CreateBookViewModel, FragmentBookStickerBinding>() {
    override val layoutId = R.layout.fragment_book_sticker
    private val list = stickerList.map { StickerItem(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stickerRv.run {
            adapter = BookStickerAdapter().apply {
                submitList(list)
                setViewHolderViewModel(this@BookStickerFragment.viewModel)
            }
        }
    }
}