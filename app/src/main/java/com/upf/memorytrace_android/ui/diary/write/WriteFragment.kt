package com.upf.memorytrace_android.ui.diary.write

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.upf.memorytrace_android.MemoryTraceApplication
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentWriteBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.applyAdjustPanMode
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.diary.write.color.ColorAdapter
import com.upf.memorytrace_android.ui.diary.write.color.ColorItem
import com.upf.memorytrace_android.ui.diary.write.image.ImageType
import com.upf.memorytrace_android.ui.diary.write.image.WriteImageBottomSheetFragment
import com.upf.memorytrace_android.ui.diary.write.sticker.WriteStickerBottomSheetFragment
import com.upf.memorytrace_android.util.*
import com.xiaopo.flying.sticker.DrawableSticker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding
    private val navArgs by navArgs<WriteFragmentArgs>()

    private val viewModel: WriteViewModel by viewModels()

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            with(result) {
                val imageUri = viewModel.imageUri
                if (imageUri == null || resultCode != Activity.RESULT_OK) {
                    toast(getString(R.string.write_load_image_fail))
                    return@registerForActivityResult
                }
                cropImageWithUri(imageUri)
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

    private val selectImageDialog by lazy(LazyThreadSafetyMode.NONE) {
        WriteImageBottomSheetFragment(viewModel)
    }

    private val stickerDialog by lazy(LazyThreadSafetyMode.NONE) {
        WriteStickerBottomSheetFragment(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (requireActivity().application as? MemoryTraceApplication)?.appComponent?.fragmentComponent()?.create()?.inject(this)

        binding = FragmentWriteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@WriteFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyAdjustPanMode()

        viewModel.navArgs(navArgs)
        setProperties()
        setBackPressedDispatcher()

        observe(viewModel.showCantEditable) {
            showDialog(
                requireActivity(),
                R.string.crop_title,
                R.string.write_unmodifiable_image_dialog_message,
                R.string.modify_image
            ) {
                viewModel.resetImages()
                showSelectImageDialog()
            }
        }
        observe(viewModel.isShowSelectImgDialog) { showSelectImageDialog() }
        observe(viewModel.isLoadGallery) { accessGallery() }
        observe(viewModel.isLoadCamera) { checkCameraPermission(ImageType.CAMERA) }
        observe(viewModel.addSticker) { attachSticker(it) }
        observe(viewModel.isShowColorDialog) { showColorDialog(it) }
        observe(viewModel.color) { it?.let { color -> changeColor(color) } }
        observe(viewModel.isSaveDiary) { saveDiary() }
        observe(viewModel.isExit) { showExitDialog() }
        observe(viewModel.isShowStickerDialog) { if (it) loadStickerDialog() else closeStickerDialog() }
        observe(viewModel.navDirections) { navigation(it) }
        observe(viewModel.toast) { toast(it) }
        observe(viewModel.cantEditableImageUrl) {
            it?.let {
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.image)
            }
        }

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
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Bitmap>("image")
            ?.observe(viewLifecycleOwner) {
                viewModel.resetImages()
                viewModel.bitmap.value = it
            }
    }

    private fun setBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    private fun cropImageWithUri(uri: Uri?) {
        viewModel.navDirections.value =
            WriteFragmentDirections.actionWriteFragmentToCropFragment(uri, null)
    }

    private fun showSelectImageDialog() {
        selectImageDialog.show(parentFragmentManager, SELECT_IMG_DIALOG_TAG)
    }

    private fun dismissSelectImageDialog() {
        selectImageDialog.dismiss()
    }

    private fun accessCamera() {
        dismissSelectImageDialog()
        val imageUri = viewModel.getImageNewImageUri()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                cameraActivityResultLauncher.launch(takePictureIntent)
            }
        }
    }

    private fun accessGallery() {
        dismissSelectImageDialog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            loadGallery()
        } else {
            checkCameraPermission(ImageType.GALLERY)
        }
    }

    private fun checkCameraPermission(type: ImageType) {
        TedPermission.with(context)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    when (type) {
                        ImageType.CAMERA -> accessCamera()
                        ImageType.GALLERY -> loadGallery()
                    }
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {}
            })
            .setDeniedMessage(NOTICE_DO_NOT_LOAD_GALLERY)
            .apply {
                when (type) {
                    ImageType.CAMERA -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            setPermissions(Manifest.permission.CAMERA)
                        else
                            setPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                    }
                    ImageType.GALLERY -> setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            .check()
    }

    private fun loadGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
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
        if (!isSingleClick()) return
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

    //Todo : ViewModel 의 navigation 의존을 덜어낸 후에 제거가 가능합니다
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

    companion object {
        private const val NOTICE_DO_NOT_LOAD_GALLERY = "이미지를 로드하려면 권한이 필요합니다."
        private const val NOTICE_ADD_POLAROID = "사진이나 단색을 먼저 입력해주세요"
        private const val SELECT_IMG_DIALOG_TAG = "SelectImageDialog"
        private const val STICKER_DIALOG_TAG = "StickerDialog"
    }
}