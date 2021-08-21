package com.upf.memorytrace_android.ui.book

import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentBookSettingBinding
import com.upf.memorytrace_android.viewmodel.BookSettingViewModel

internal class BookSettingFragment : BaseFragment<BookSettingViewModel, FragmentBookSettingBinding>() {
    override val layoutId = R.layout.fragment_book_setting
    override val viewModelClass = BookSettingViewModel::class
    override val navArgs by navArgs<BookSettingFragmentArgs>()
}