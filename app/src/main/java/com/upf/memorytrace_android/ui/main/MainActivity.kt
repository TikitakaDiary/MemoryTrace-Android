package com.upf.memorytrace_android.ui.main

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseActivity
import com.upf.memorytrace_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main
    override val viewModelClass = MainViewModel::class
}