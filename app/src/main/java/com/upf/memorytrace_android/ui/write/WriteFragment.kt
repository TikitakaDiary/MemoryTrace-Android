package com.upf.memorytrace_android.ui.write

import android.Manifest
import android.app.Activity
import androidx.activity.addCallback
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentWriteBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.ImageConverter
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.TimeUtil
import com.upf.memorytrace_android.viewmodel.WriteViewModel
import com.xiaopo.flying.sticker.DrawableSticker

internal class WriteFragment : BaseFragment<WriteViewModel, FragmentWriteBinding>() {
    override val layoutId = R.layout.fragment_write
    override val viewModelClass = WriteViewModel::class
    override val navArgs by navArgs<WriteFragmentArgs>()

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
    private val stickerDialog by lazy(LazyThreadSafetyMode.NONE) {
        WriteStickerBottomSheetFragment(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProperties()
        setBackPressedDispatcher()

        observe(viewModel.isShowSelectImgDialog) { showSelectImageDialog() }
        observe(viewModel.isLoadGallery) { accessGallery() }
        observe(viewModel.isLoadCamera) { accessCamera() }
        observe(viewModel.addSticker) { attachSticker(it) }
        observe(viewModel.isShowColorDialog) { showColorDialog(it) }
        observe(viewModel.color) { it?.let { color -> changeColor(color) } }
        observe(viewModel.isSaveDiary) { saveDiary() }
        observe(viewModel.isExit) { showExitDialog() }
        observe(viewModel.isShowStickerDialog) { if (it) loadStickerDialog() else closeStickerDialog() }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        with(viewModel.stickerList) {
            clear()
            addAll(binding.stickerView.stickers)
        }
    }

    private fun setProperties() {
        with(binding) {
            dateTv.text = TimeUtil.getTodayDate(TimeUtil.YYYY_M_D_KR)
            nameTv.text = MemoryTraceConfig.nickname
            stickerView.stickers = this@WriteFragment.viewModel.stickerList
            colorRv.adapter = ColorAdapter().apply {
                setViewHolderViewModel(this@WriteFragment.viewModel)
                submitList(Colors.getColors().map { ColorItem(it) })
            }
        }
        receiveArgFromOtherView<Bitmap>("image") { viewModel.bitmap.value = it }
    }

    private fun setBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
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

    private fun showColorDialog(isShow: Boolean) {
        if (isShow) selectImageDialog.dismiss()
    }

    private fun loadStickerDialog() {
        if (viewModel.bitmap.value == null && viewModel.color.value == null) {
            toast(NOTICE_ADD_POLAROID)
        } else {
            stickerDialog.show(parentFragmentManager, STICKER_DIALOG_TAG)
        }
    }

    private fun closeStickerDialog() {
        stickerDialog.dismiss()
    }

    private fun attachSticker(@DrawableRes stickerId: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), stickerId)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }

    private fun changeColor(color: Colors) {
        Colors.fillColor(binding.colorView, color)
    }

    private fun saveDiary() {
        binding.progressbar.isVisible = true
        binding.stickerView.removeStickerHandler()
        val bitmap = ImageConverter.convertViewToBitmap(binding.cardView)
        val cacheDir = requireContext().cacheDir
        viewModel.uploadDiary(cacheDir, bitmap)
    }

    private fun showExitDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.ExitAlertDialog).apply {
                setTitle(R.string.write_exit_title)
                setMessage(R.string.write_exit_content)
                setPositiveButton(R.string.write_exit_exit) { _, _ -> viewModel.onClickBack() }
                setNegativeButton(R.string.write_exit_cancel, null)
            }
            builder.create().show()
        }
    }

    companion object {
        private const val NOTICE_DO_NOT_LOAD_GALLERY = "이미지를 로드하려면 권한이 필요합니다."
        private const val NOTICE_ADD_POLAROID = "사진이나 단색을 먼저 입력해주세요"
        private const val SELECT_IMG_DIALOG_TAG = "SelectImageDialog"
        private const val STICKER_DIALOG_TAG = "StickerDialog"
    }
}