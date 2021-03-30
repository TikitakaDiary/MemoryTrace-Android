package com.upf.memorytrace_android.ui

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseActivity
import com.upf.memorytrace_android.databinding.ActivityMainBinding
import com.upf.memorytrace_android.viewmodel.MainViewModel

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main
    override val viewModelClass = MainViewModel::class
}