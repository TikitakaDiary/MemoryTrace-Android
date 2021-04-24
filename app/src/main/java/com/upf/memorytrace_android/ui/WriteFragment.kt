package com.upf.memorytrace_android.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentWriteBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.viewmodel.WriteViewModel
import com.xiaopo.flying.sticker.DrawableSticker

internal class WriteFragment : BaseFragment<WriteViewModel, FragmentWriteBinding>() {
    override val layoutId = R.layout.fragment_write
    override val viewModelClass = WriteViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.isAttachSticker) { attachSticker() }
    }

    private fun attachSticker() {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }
}