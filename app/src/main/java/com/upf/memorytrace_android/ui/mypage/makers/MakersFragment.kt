package com.upf.memorytrace_android.ui.mypage.makers

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentMakersBinding

internal class MakersFragment : BaseFragment<MakersViewModel, FragmentMakersBinding>() {
    override val layoutId = R.layout.fragment_makers
    override val viewModelClass = MakersViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}