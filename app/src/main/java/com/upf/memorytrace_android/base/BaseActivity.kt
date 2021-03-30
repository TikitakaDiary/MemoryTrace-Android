package com.upf.memorytrace_android.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.upf.memorytrace_android.BR

internal abstract class BaseActivity<VM : BaseViewModel, VB : ViewDataBinding> :
    AppCompatActivity() {
    abstract val layoutId: Int
    abstract val viewModel: VM

    lateinit var binding: VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
    }

}