package com.upf.memorytrace_android.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentWriteBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.util.TimeUtil
import com.upf.memorytrace_android.viewmodel.WriteViewModel
import com.xiaopo.flying.sticker.DrawableSticker

internal class WriteFragment : BaseFragment<WriteViewModel, FragmentWriteBinding>() {
    override val layoutId = R.layout.fragment_write
    override val viewModelClass = WriteViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setProperties()
        observe(viewModel.isShowSelectImgDialog) { showSelectImageDialog() }
        observe(viewModel.isLoadGallery) { if (checkReadPermission()) accessGallery() }
        observe(viewModel.isLoadCamera) { }
        observe(viewModel.isAttachSticker) { attachSticker() }
    }

    private fun setProperties() {
        with(binding) {
            dateTv.text = TimeUtil.getTodayDate(TimeUtil.YYYY_M_D_KR)
            nameTv.text = "유진진"
        }
    }

    private fun showSelectImageDialog() {
        WriteImageBottomSheetFragment(viewModel).show(parentFragmentManager, SELECT_IMG_DIALOG_TAG)
    }

    private fun checkReadPermission(): Boolean {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val selfPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return if (selfPermission != PackageManager.PERMISSION_GRANTED) {
            toast(NOTICE_DO_NOT_LOAD_GALLERY)
            requestPermissions(permissions, REQUEST_CODE_READ_EXTERNAL_STORAGE)
            false
        } else {
            true
        }
    }

    private fun accessGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun attachSticker() {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
        private const val NOTICE_DO_NOT_LOAD_GALLERY = "이미지를 로드하려면 권한이 필요합니다."
        private const val SELECT_IMG_DIALOG_TAG = "SelectImageDialog"
    }
}