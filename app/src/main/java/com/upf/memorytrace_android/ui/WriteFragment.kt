package com.upf.memorytrace_android.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            with(result) {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let { cropImageWithBitmap(it.extras?.get("data") as? Bitmap) }
                }
            }
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            with(result) {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let { cropImageWithUri(it.data) }
                }
            }
        }
    private val readPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) loadGallery()
        }

    private val selectImageDialog by lazy(LazyThreadSafetyMode.NONE) {
        WriteImageBottomSheetFragment(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProperties()
        observe(viewModel.isShowSelectImgDialog) { showSelectImageDialog() }
        observe(viewModel.isLoadGallery) { accessGallery() }
        observe(viewModel.isLoadCamera) { accessCamera() }
        observe(viewModel.isAttachSticker) { attachSticker() }
    }

    private fun setProperties() {
        with(binding) {
            dateTv.text = TimeUtil.getTodayDate(TimeUtil.YYYY_M_D_KR)
            nameTv.text = "유진진"
        }
        receiveArgFromOtherView<Bitmap>("image") { viewModel.bitmap.value = it }
    }

    private fun cropImageWithBitmap(bitmap: Bitmap?) {
        cropImage(null, bitmap)
    }

    private fun cropImageWithUri(uri: Uri?) {
        cropImage(uri, null)
    }

    private fun cropImage(uri: Uri?, bitmap: Bitmap?) {
        viewModel.navDirections.value =
            WriteFragmentDirections.actionWriteFragmentToCropFragment(uri, bitmap)
    }

    private fun showSelectImageDialog() {
        selectImageDialog.show(parentFragmentManager, SELECT_IMG_DIALOG_TAG)
    }

    private fun dismissSelectImageDialog() {
        selectImageDialog.dismiss()
    }

    private fun checkReadPermission() {
        val selfPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (selfPermission != PackageManager.PERMISSION_GRANTED) {
            toast(NOTICE_DO_NOT_LOAD_GALLERY)
            readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            loadGallery()
        }
    }

    private fun accessCamera() {
        dismissSelectImageDialog()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireActivity().packageManager)?.also {
                cameraActivityResultLauncher.launch(intent)
            }
        }
    }

    private fun accessGallery() {
        dismissSelectImageDialog()
        checkReadPermission()
    }

    private fun loadGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun attachSticker() {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }

    companion object {
        private const val NOTICE_DO_NOT_LOAD_GALLERY = "이미지를 로드하려면 권한이 필요합니다."
        private const val SELECT_IMG_DIALOG_TAG = "SelectImageDialog"
    }
}