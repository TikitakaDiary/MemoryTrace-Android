package com.upf.memorytrace_android.ui.book.setting

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentBookSettingBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.util.showDialog

internal class BookSettingFragment :
    BaseFragment<BookSettingViewModel, FragmentBookSettingBinding>() {
    override val layoutId = R.layout.fragment_book_setting
    override val viewModelClass = BookSettingViewModel::class
    override val navArgs by navArgs<BookSettingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.showLeaveDialog) {
            showDialog(
                requireActivity(),
                R.string.leave_book_section_title,
                R.string.leave_book_message,
                R.string.write_exit_exit
            ) { viewModel.leaveBook() }
        }
    }
}