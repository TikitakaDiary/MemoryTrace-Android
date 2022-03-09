package com.upf.memorytrace_android.ui.login.presenter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentTermsAgreeBinding
import com.upf.memorytrace_android.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAgreeFragment :
    BindingFragment<FragmentTermsAgreeBinding>(R.layout.fragment_terms_agree) {

    private val viewModel: TermsAgreeViewModel by viewModels()
    private val args: TermsAgreeFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.btnAgree.isEnabled = false
        binding.btnAll.setOnClickListener {
            val changedState = !binding.checkboxAll.isChecked
            binding.checkboxAll.isChecked = changedState
            binding.checkboxService.isChecked = changedState
            binding.checkboxPrivacy.isChecked = changedState
        }

        binding.checkboxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.btnAgree.isEnabled = isChecked
        }
        binding.checkboxService.setOnCheckedChangeListener { buttonView, isChecked ->
            setAgreeAllCheckboxState()
        }
        binding.checkboxPrivacy.setOnCheckedChangeListener { buttonView, isChecked ->
            setAgreeAllCheckboxState()
        }

        binding.btnAgree.setOnClickListener {
            setAgreeAllCheckboxState()
            if (binding.checkboxAll.isChecked) {
                val user = args.user
                if (user == null) {
                    Toast.makeText(requireContext(), "잘못된 접근입니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                } else {
                    viewModel.register(user)
                }
            } else {
                Toast.makeText(requireContext(), "약관 동의를 해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDisagree.setOnClickListener {
            Toast.makeText(requireContext(), "약관 동의를 해주세요!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        observeData()
    }

    private fun observeData() {
        viewModel.moveToTermsAgreePage.observe(viewLifecycleOwner, { terms ->
            if (terms != 1 && terms != 2) return@observe
            viewModel.moveToTermsAgreePage.value = 0
            findNavController().navigate(
                TermsAgreeFragmentDirections.actionTermsAgreeFragmentToTermsFragment(
                    terms
                )
            )
        })
    }

    private fun setAgreeAllCheckboxState() {
        binding.checkboxAll.isChecked =
            binding.checkboxService.isChecked && binding.checkboxPrivacy.isChecked
    }

}