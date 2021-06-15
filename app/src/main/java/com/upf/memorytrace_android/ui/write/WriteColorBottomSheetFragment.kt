package com.upf.memorytrace_android.ui.write

import android.os.Bundle
import android.view.View
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetWriteColorBinding
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.viewmodel.WriteViewModel

internal class WriteColorBottomSheetFragment(
    override val viewModel: WriteViewModel
) : BaseBottomSheetFragment<WriteViewModel, FragmentBottomSheetWriteColorBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_write_color

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        binding.colorRv.adapter = ColorAdapter().apply {
            setViewHolderViewModel(this@WriteColorBottomSheetFragment.viewModel)
            submitList(Colors.getColors().map { ColorItem(it) })
        }
    }
}