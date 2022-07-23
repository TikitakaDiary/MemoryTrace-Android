package com.upf.memorytrace_android.ui.diary.write.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.upf.memorytrace_android.databinding.DialogSelectImageTypeBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.diary.write.DiaryWriteViewModel

class SelectImageTypeDialogFragment: BottomSheetDialogFragment() {

    private val viewModel: DiaryWriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogSelectImageTypeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSelectImageTypeBinding.bind(view)

        binding.selectImageTypeCamera.setOnDebounceClickListener {
            viewModel.onClickSelectCameraType()
            dismissAllowingStateLoss()
        }

        binding.selectImageTypeAlbum.setOnDebounceClickListener {
            viewModel.onClickSelectAlbumType()
            dismissAllowingStateLoss()
        }

        binding.selectImageTypeColor.setOnDebounceClickListener {
            viewModel.onClickSelectColorType()
            dismissAllowingStateLoss()
        }
    }
}