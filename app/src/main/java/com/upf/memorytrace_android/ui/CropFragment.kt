package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentCropBinding
import com.upf.memorytrace_android.viewmodel.CropViewModel

internal class CropFragment : BaseFragment<CropViewModel, FragmentCropBinding>() {
    override val layoutId = R.layout.fragment_crop
    override val viewModelClass = CropViewModel::class
}