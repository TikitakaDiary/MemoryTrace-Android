package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentSplashBinding
import com.upf.memorytrace_android.viewmodel.SplashViewModel

internal class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {
    override val layoutId = R.layout.fragment_splash
    override val viewModelClass = SplashViewModel::class
}