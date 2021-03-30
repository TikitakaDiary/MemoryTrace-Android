package com.upf.memorytrace_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.BR
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.EmptyNavArgs

internal abstract class BaseFragment<VM : BaseViewModel, VB : ViewDataBinding> : Fragment() {
    abstract val layoutId: Int
    abstract val viewModel: VM
    protected open val navArgs: NavArgs = EmptyNavArgs

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(viewModel) {
            observe(toast) { toast(it) }
            observe(navDirections) { navigation(it) }
        }

        viewModel.navArgs(navArgs)
    }

    fun <T> sendArgToBackStack(key: String, value: T) {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<T>(key)
            ?.value = value
    }

    fun <T> receiveArgFromOtherView(key: String, callback: (T) -> Unit) {
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner, Observer { callback(it) })
    }

    private fun navigation(navDirections: NavDirections) {
        if (navDirections is BackDirections) {
            if (navDirections.destinationId == -1) {
                findNavController().popBackStack()
            } else {
                with(navDirections) {
                    findNavController().popBackStack(destinationId, inclusive)
                }
            }
        } else {
            findNavController().navigate(navDirections)
        }
    }
}