package com.upf.memorytrace_android.ui.mypage.terms

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentTermsBinding
import com.upf.memorytrace_android.ui.base.BaseFragment

internal class TermsFragment : BaseFragment<TermsViewModel, FragmentTermsBinding>() {
    override val layoutId = R.layout.fragment_terms
    override val viewModelClass = TermsViewModel::class
    override val navArgs by navArgs<TermsFragmentArgs>()
    private val ERROR = "예상하지 못한 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        when (navArgs.terms) {
            1 -> binding.termsText.text = getString(R.string.mypage_term_service)
            2 -> binding.termsText.text = getString(R.string.mypage_term_privacy)
            else -> binding.termsText.text = ERROR
        }

    }
}