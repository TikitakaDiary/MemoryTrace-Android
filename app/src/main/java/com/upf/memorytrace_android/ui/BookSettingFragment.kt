package com.upf.memorytrace_android.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentBookSettingBinding
import com.upf.memorytrace_android.databinding.FragmentMypageBinding
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.viewmodel.BookSettingViewModel
import com.upf.memorytrace_android.viewmodel.MypageViewModel

internal class BookSettingFragment :
    BaseFragment<BookSettingViewModel, FragmentBookSettingBinding>() {
    override val layoutId = R.layout.fragment_book_setting
    override val viewModelClass = BookSettingViewModel::class


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCoverEdit.setOnClickListener {
            sendArgToBackStack("book", viewModel.book)
            viewModel.navDirections.value =
                BookSettingFragmentDirections.actionBookSettingFragmentToCreateBookFragment()
        }
    }

}