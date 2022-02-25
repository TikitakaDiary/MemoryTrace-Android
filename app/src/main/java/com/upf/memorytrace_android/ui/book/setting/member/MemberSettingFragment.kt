package com.upf.memorytrace_android.ui.book.setting.member

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentMemberSettingBinding
import com.upf.memorytrace_android.extension.copyToClipboard
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.base.BaseFragment

internal class MemberSettingFragment :
    BaseFragment<MemberSettingViewModel, FragmentMemberSettingBinding>() {
    override val layoutId = R.layout.fragment_member_setting
    override val viewModelClass = MemberSettingViewModel::class
    override val navArgs by navArgs<MemberSettingFragmentArgs>()

    private val adapter = BookMemberAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        observe(viewModel.userList) { list ->
            adapter.submitList(list.map { BookMemberItem(it.uid, it.nickname) })
        }
        observe(viewModel.invite) {
            requireActivity().copyToClipboard("invite_code", it)
            toast(getString(R.string.toast_copy_invitation_code))
        }
    }

    private fun initializeRecyclerView() {
        binding.memberRv.adapter = adapter
    }
}