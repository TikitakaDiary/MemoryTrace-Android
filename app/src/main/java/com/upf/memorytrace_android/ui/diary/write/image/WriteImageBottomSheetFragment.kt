package com.upf.memorytrace_android.ui.diary.write.image

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseBottomSheetFragment
import com.upf.memorytrace_android.databinding.FragmentBottomSheetWriteImageBinding
import com.upf.memorytrace_android.ui.diary.write.WriteViewModel

internal class WriteImageBottomSheetFragment(override val viewModel: WriteViewModel) :
    BaseBottomSheetFragment<WriteViewModel, FragmentBottomSheetWriteImageBinding>() {
    override val layoutId = R.layout.fragment_bottom_sheet_write_image
}