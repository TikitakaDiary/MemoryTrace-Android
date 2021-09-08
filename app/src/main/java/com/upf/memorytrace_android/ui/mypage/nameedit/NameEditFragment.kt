package com.upf.memorytrace_android.ui.mypage.nameedit

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentNameEditBinding
import com.upf.memorytrace_android.extension.toast

internal class NameEditFragment : BaseFragment<NameEditViewModel, FragmentNameEditBinding>() {
    override val layoutId = R.layout.fragment_name_edit
    override val viewModelClass = NameEditViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSave.setOnClickListener {
            if (binding.edName.text.isNullOrBlank()) {
                toast(getString(R.string.mypage_name_edit_hint))
            } else {
                viewModel.editName(binding.edName.text.toString())
                sendArgToBackStack("reload", true)
            }
        }

    }
}