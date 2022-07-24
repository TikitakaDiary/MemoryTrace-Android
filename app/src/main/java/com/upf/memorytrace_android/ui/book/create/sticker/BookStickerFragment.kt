package com.upf.memorytrace_android.ui.book.create.sticker

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseViewPagerFragment
import com.upf.memorytrace_android.databinding.FragmentBookStickerBinding
import com.upf.memorytrace_android.sticker.SelectStickerDialogFragment
import com.upf.memorytrace_android.ui.book.create.CreateBookViewModel
import com.upf.memorytrace_android.sticker.StickerItem

@Deprecated(
    "SelectStickerDialogFragment 를 사용하세요.",
    ReplaceWith(
        "SelectStickerDialogFragment",
        "com.upf.memorytrace_android.sticker.SelectStickerDialogFragment"
    )
)
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