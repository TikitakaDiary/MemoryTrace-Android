package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentDiaryBinding
import com.upf.memorytrace_android.viewmodel.DiaryViewModel

internal class DiaryFragment : BaseFragment<DiaryViewModel, FragmentDiaryBinding>() {
    override val layoutId = R.layout.fragment_diary
    override val viewModelClass = DiaryViewModel::class
}