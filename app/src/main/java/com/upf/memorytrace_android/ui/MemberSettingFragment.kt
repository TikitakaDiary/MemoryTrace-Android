package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentMemberSettingBinding
import com.upf.memorytrace_android.viewmodel.MemberSettingViewModel

internal class MemberSettingFragment : BaseFragment<MemberSettingViewModel, FragmentMemberSettingBinding>() {
    override val layoutId = R.layout.fragment_member_setting
    override val viewModelClass = MemberSettingViewModel::class
}