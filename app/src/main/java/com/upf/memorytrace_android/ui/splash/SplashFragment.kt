package com.upf.memorytrace_android.ui.splash

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentSplashBinding

internal class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {
    override val layoutId = R.layout.fragment_splash
    override val viewModelClass = SplashViewModel::class
}