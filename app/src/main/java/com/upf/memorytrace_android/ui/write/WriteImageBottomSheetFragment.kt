package com.upf.memorytrace_android.ui.write

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetWriteImageBinding
import com.upf.memorytrace_android.viewmodel.WriteViewModel

internal class WriteImageBottomSheetFragment(override val viewModel: WriteViewModel) :
    BaseBottomSheetFragment<WriteViewModel, FragmentBottomSheetWriteImageBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_write_image
}