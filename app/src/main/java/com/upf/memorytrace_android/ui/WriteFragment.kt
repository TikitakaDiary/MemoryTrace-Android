package com.upf.memorytrace_android.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentWriteBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.util.TimeUtil
import com.upf.memorytrace_android.viewmodel.WriteViewModel
import com.xiaopo.flying.sticker.DrawableSticker

internal class WriteFragment : BaseFragment<WriteViewModel, FragmentWriteBinding>() {
    override val layoutId = R.layout.fragment_write
    override val viewModelClass = WriteViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setProperties()
        observe(viewModel.isAttachSticker) { attachSticker() }
    }

    private fun setProperties() {
        with(binding) {
            dateTv.text = TimeUtil.getTodayDate(TimeUtil.YYYY_M_D_KR)
            nameTv.text = "유진진"
        }
    }

    private fun attachSticker() {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }
}