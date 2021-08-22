package com.upf.memorytrace_android.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentMypageBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.showDialog
import com.upf.memorytrace_android.viewmodel.MypageViewModel

internal class MypageFragment : BaseFragment<MypageViewModel, FragmentMypageBinding>() {
    override val layoutId = R.layout.fragment_mypage
    override val viewModelClass = MypageViewModel::class


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        receiveArgFromOtherView<Boolean>("reload") {
            if (it) viewModel.name.value = MemoryTraceConfig.nickname
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnInvite.setOnClickListener {
            if (binding.edInvite.text.isNullOrBlank())
                toast(getString(R.string.mypage_invite_code_hint))
            else {
                viewModel.joinToBook(binding.edInvite.text.toString())
            }
        }

        binding.btnNameEdit.setOnClickListener {
            findNavController().navigate(MypageFragmentDirections.actionMypageFragmentToNameEditFragment())
        }
        binding.btnMakers.setOnClickListener {
            findNavController().navigate(MypageFragmentDirections.actionMypageFragmentToMakersFragment())
        }

        binding.btnLogout.setOnClickListener {
            showDialog(
                requireActivity(),
                R.string.mypage_logout,
                R.string.mypage_logout_message,
                R.string.mypage_logout
            ) {
                viewModel.resetUser()
            }
        }
        binding.btnLeave.setOnClickListener {
            showDialog(
                requireActivity(),
                R.string.mypage_leave,
                R.string.mypage_leave_message,
                R.string.mypage_leave_confirm
            ) {
                viewModel.withdrawalUser()
            }
        }
        observe(viewModel.showOssPage){ startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java)) }

    }


}