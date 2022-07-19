package com.upf.memorytrace_android.ui.mypage.themechange

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentThemeChangeBinding
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.ThemeUtil

internal class ThemeChangeFragment :
    BaseFragment<ThemeChangeViewModel, FragmentThemeChangeBinding>() {
    override val layoutId = R.layout.fragment_theme_change
    override val viewModelClass = ThemeChangeViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSave.setOnClickListener {
            MemoryTraceConfig.theme = viewModel.theme.value ?: ThemeUtil.ThemeMode.DEFAULT.ordinal
            ThemeUtil.applyTheme()
            findNavController().popBackStack()
        }
    }
}
