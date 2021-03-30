package com.upf.memorytrace_android.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.upf.memorytrace_android.BR
import com.upf.memorytrace_android.MemoryTraceApplication
import com.upf.memorytrace_android.di.MainComponent
import kotlin.reflect.KClass

internal abstract class BaseActivity<VM : BaseViewModel, VB : ViewDataBinding> :
    AppCompatActivity() {
    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<VM>

    lateinit var binding: VB

    lateinit var mainComponent: MainComponent
    protected val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(viewModelClass.java)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainComponent =
            (application as MemoryTraceApplication).appComponent.mainComponent().create()
        mainComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
    }

}