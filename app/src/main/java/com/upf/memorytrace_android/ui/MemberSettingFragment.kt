package com.upf.memorytrace_android.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentMemberSettingBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.viewmodel.MemberSettingViewModel

internal class MemberSettingFragment :
    BaseFragment<MemberSettingViewModel, FragmentMemberSettingBinding>() {
    override val layoutId = R.layout.fragment_member_setting
    override val viewModelClass = MemberSettingViewModel::class
    override val navArgs by navArgs<MemberSettingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.invite) {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("invite_code", it)
            clipboard.setPrimaryClip(clip)
        }
    }
}