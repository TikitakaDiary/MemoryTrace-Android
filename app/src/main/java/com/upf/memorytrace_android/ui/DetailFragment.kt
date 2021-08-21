package com.upf.memorytrace_android.ui

import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentDetailBinding
import com.upf.memorytrace_android.viewmodel.DetailViewModel

internal class DetailFragment : BaseFragment<DetailViewModel, FragmentDetailBinding>() {
    override val layoutId = R.layout.fragment_detail
    override val viewModelClass = DetailViewModel::class
    override val navArgs by navArgs<DetailFragmentArgs>()
}