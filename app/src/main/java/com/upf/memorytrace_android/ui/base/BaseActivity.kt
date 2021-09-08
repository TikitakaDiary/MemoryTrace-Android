package com.upf.memorytrace_android.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.upf.memorytrace_android.BR
import com.upf.memorytrace_android.MemoryTraceApplication
import com.upf.memorytrace_android.di.ActivityComponent
import kotlin.reflect.KClass

internal abstract class BaseActivity<VM : BaseViewModel, VB : ViewDataBinding> :
    AppCompatActivity() {
    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<VM>

    lateinit var binding: VB

    lateinit var activityComponent: ActivityComponent
    protected val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(viewModelClass.java)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent =
            (application as MemoryTraceApplication).appComponent.activityComponent().create()
        activityComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
    }

}