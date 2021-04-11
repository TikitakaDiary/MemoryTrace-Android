package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentLoginBinding
import com.upf.memorytrace_android.viewmodel.LoginViewModel

internal class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {
    override val layoutId = R.layout.fragment_login
    override val viewModelClass = LoginViewModel::class
}