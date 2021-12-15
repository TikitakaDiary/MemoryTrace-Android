package com.upf.memorytrace_android.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BindingFragment<B : ViewDataBinding>(@LayoutRes private val contentLayoutId: Int) : Fragment() {

    private var _binding: B? = null
    protected val binding: B
        get() {
            if (_binding == null) {
                throw NullPointerException("binding is null. Please refer it in ViewLifeCycle (onCreateView ~ onDestroyView)")
            } else {
                return _binding!!
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}