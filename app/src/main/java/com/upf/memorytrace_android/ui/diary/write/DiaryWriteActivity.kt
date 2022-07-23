package com.upf.memorytrace_android.ui.diary.write

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.color.toColorInt
import com.upf.memorytrace_android.databinding.ActivityDiaryWriteBinding
import com.upf.memorytrace_android.databinding.LayoutDiaryWriteSelectColorContainerBinding
import com.upf.memorytrace_android.extension.*
import com.upf.memorytrace_android.ui.diary.write.color.ColorAdapter
import com.upf.memorytrace_android.ui.diary.write.image.ImageCropActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.upf.memorytrace_android.util.showDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DiaryWriteActivity : AppCompatActivity() {

    private val viewModel: DiaryWriteViewModel by viewModels()

    private var selectColorBinding: LayoutDiaryWriteSelectColorContainerBinding? = null

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.currentCameraImageFileUri?.cropImage()
            } else {
                viewModel.clearCameraTempFileUri()
            }
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            with(result) {
                val selectedImageUri: Uri? = data?.data
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri?.cropImage()
                }
            }
        }

    private val imageCropActivityResultLauncher =
        registerForActivityResult(ImageCropActivity.ImageCropContract()) { croppedImageUri ->
            croppedImageUri?.let { viewModel.applyContentImage(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val diaryId: Int? =
            intent.getIntExtra(EXTRA_INPUT_DIARY_ID, -1).takeIf { it != -1 }
        val originalDiary: DiaryWriteContentUiModel? =
            intent.getParcelableExtra(EXTRA_INPUT_ORIGINAL_DIARY)

        repeatOnStart {
            launch {
                viewModel.contentUiModel.collect {
                    binding.writeTitle.setTextIfNew(it.title)
                    binding.writeContents.setTextIfNew(it.content)
                    binding.writeWriter.text = it.writerName
                    binding.writeDate.text = it.date
                    binding.setPolaroidImage(it.image)
                }
            }

            launch {
                viewModel.toolbarState.collect { toolbarState ->
                    binding.writeToolbarTitle.isVisible =
                        toolbarState != DiaryWriteToolbarState.EDIT

                    when (toolbarState) {
                        DiaryWriteToolbarState.WRITE -> {
                            binding.writeToolbarTitle.text = getString(R.string.write_create_diary)
                            binding.setBackButtonArrow()
                            binding.setToolbarButton(R.string.write_delivery) {
                                // Todo
                            }
                        }
                        DiaryWriteToolbarState.EDIT -> {
                            binding.setBackButtonCancel()
                            binding.setToolbarButton(R.string.write_save) {
                                // Todo
                            }
                        }
                        DiaryWriteToolbarState.SELECT_COLOR -> {
                            binding.writeToolbarTitle.text =
                                getString(R.string.write_select_color_title)
                            binding.setToolbarButton(R.string.write_save) {
                                viewModel.onSaveSelectColorClick()
                            }
                            binding.setBackButtonClose {
                                viewModel.dismissSelectColorLayout()
                            }
                        }
                        DiaryWriteToolbarState.ATTACH_STICKER -> {
                            binding.setBackButtonClose { }
                            // Todo
                        }
                    }
                }
            }

            launch {
                viewModel.selectColorUiModel.collect { uiModel ->
                    binding.applySelectColorBinding(isNeedInflate = uiModel.isShowing) {
                        if (uiModel.isShowing) {
                            it.root.isVisible = true
                            val colorAdapter =
                                (it.recyclerviewSelectColor.adapter as? ColorAdapter)
                            colorAdapter?.submitList(uiModel.colorList)
                            clearFocusAndHideSoftInput()
                        } else {
                            it.root.isVisible = false
                        }
                    }
                }
            }
        }

        observeEvent(viewModel.event) { event ->
            when (event) {
                is DiaryWriteEvent.ShowEditConfirmDialog -> {
                    showDialog(
                        context = this,
                        title = R.string.crop_title,
                        message = R.string.write_unmodifiable_image_dialog_message,
                        confirm = R.string.modify_image,
                        positive = {
                            viewModel.onClickEditConfirmYes(event.nextImageType)
                        }
                    )
                }
                is DiaryWriteEvent.StartCameraActivity -> {
                    if (isGrantedPermission(Manifest.permission.CAMERA)) {
                        takePhoto()
                    } else {
                        requestPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA)
                    }
                }
                is DiaryWriteEvent.StartGalleryActivity -> {
                    startGalleryActivityForResult()
                }
            }
        }

        observeEvent(viewModel.errorEvent) { event ->
            when (event) {
                is DiaryWriteErrorEvent.WrongAccess -> {
                    toast("잘못된 접근입니다.")
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }

        binding.writeSelectImageButton.setOnDebounceClickListener {
            showSelectImageTypeDialogFragment()
        }
        binding.writePolaroidImageContainer.setOnDebounceClickListener {
            showSelectImageTypeDialogFragment()
        }

        viewModel.loadDiary(diaryId, originalDiary)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            val isGranted = grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
            val isDenied = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            when {
                isGranted -> takePhoto()
                isDenied -> toast(getString(R.string.write_toast_camera_deny))
                else -> showCameraPermissionDenyForeverInfoDialog()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onBackPressed() {
        if (viewModel.isShowingSelectColor()) {
            viewModel.dismissSelectColorLayout()
        } else {
            super.onBackPressed()
        }
    }

    private fun showSelectImageTypeDialogFragment() {
        showAllowingStateLoss("selectImageType") {
            SelectImageTypeDialogFragment()
        }
    }

    private fun showCameraPermissionDenyForeverInfoDialog() {
        showDialog(
            context = this,
            title = R.string.write_dialog_title_camera_deny_forever,
            message = R.string.write_dialog_camera_deny_forever,
            confirm = R.string.write_dialog_permission_confirm,
            positive = {
                startAppSettingActivity()
            }
        )
    }

    private fun startAppSettingActivity() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun startGalleryActivityForResult() {
        galleryActivityResultLauncher.launch(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        })
    }

    private fun takePhoto() {
        kotlin.runCatching {
            createCameraTempFile().toContentUri(this)
        }.onSuccess {
            cameraActivityResultLauncher.launch(it)
        }.onFailure {
            Log.e(javaClass.simpleName, it.stackTraceToString())
            toast(R.string.write_load_image_fail)
        }
    }

    @Throws(IOException::class)
    fun createCameraTempFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val imageDirectory: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: throw IOException("Cannot get externalFileDir (${Environment.DIRECTORY_PICTURES}")
        return File.createTempFile(timeStamp, ".jpg", imageDirectory).also {
            viewModel.saveCameraTempFileUri(Uri.fromFile(it))
        }
    }

    private fun ActivityDiaryWriteBinding.setToolbarButton(
        @StringRes labelResId: Int,
        onClick: () -> Unit
    ) {
        writeToolbarButton.text = getString(labelResId)
        writeToolbarButton.setOnDebounceClickListener {
            onClick.invoke()
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonArrow() {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_back)
            it.setOnDebounceClickListener {
                viewModel.showCancelConfirmDialogIfNeed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonClose(closeAction: () -> Unit) {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_x)
            it.setOnDebounceClickListener {
                viewModel.restorePreviousToolbarState()
                closeAction.invoke()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonCancel() {
        useTextBackButton {
            it.text = getString(R.string.write_exit_cancel)
            it.setOnDebounceClickListener {
                viewModel.showCancelConfirmDialogIfNeed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.useTextBackButton(
        action: (writeToolbarBackButtonText: TextView) -> Unit
    ) {
        with(writeToolbarBackButtonImage) {
            isVisible = false
            setOnDebounceClickListener { }
        }
        with(writeToolbarBackButtonText) {
            isVisible = true
            action.invoke(this)
        }
    }

    private fun ActivityDiaryWriteBinding.useImageBackButton(
        action: (writeToolbarBackButtonImage: ImageView) -> Unit
    ) {
        with(writeToolbarBackButtonText) {
            isVisible = false
            setOnDebounceClickListener { }
        }
        with(writeToolbarBackButtonImage) {
            isVisible = true
            action.invoke(this)
        }
    }

    private fun ActivityDiaryWriteBinding.setPolaroidImage(writeImage: WriteImageType) {
        writeNoContentsImage.isVisible = writeImage is WriteImageType.None
        writeStickerContentsImage.isVisible = writeImage !is WriteImageType.None

        when (writeImage) {
            is WriteImageType.Image -> {
                Glide.with(root.context).load(writeImage.uri).into(writePolaroidImage)
            }
            is WriteImageType.Color -> {
                writePolaroidImage.setImageDrawable(ColorDrawable(writeImage.color.toColorInt()))
            }
            else -> {
                /* Nothing to do */
            }
        }
    }

    private fun ActivityDiaryWriteBinding.applySelectColorBinding(
        isNeedInflate: Boolean,
        apply: (LayoutDiaryWriteSelectColorContainerBinding) -> Unit
    ) {
        if (isNeedInflate && selectColorBinding == null) {
            val view = stubWriteSelectColorContainer.inflate()
            selectColorBinding =
                LayoutDiaryWriteSelectColorContainerBinding.bind(view).apply {
                    recyclerviewSelectColor.itemAnimator = null
                    recyclerviewSelectColor.adapter = ColorAdapter()
                }
        }
        selectColorBinding?.apply(apply)
    }

    private fun Uri.cropImage() {
        imageCropActivityResultLauncher.launch(this)
    }

    class DiaryWriteContract : ActivityResultContract<Input, Output?>() {
        override fun createIntent(context: Context, input: Input): Intent {
            return Intent(context, DiaryWriteActivity::class.java).apply {
                if (input is Input.Edit) {
                    putExtra(EXTRA_INPUT_DIARY_ID, input.diaryId)
                    putExtra(EXTRA_INPUT_ORIGINAL_DIARY, input.originalDiary)
                }
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Output? {
            return if (intent == null || resultCode != RESULT_OK) {
                null
            } else {
                val isNewDiary = intent.getBooleanExtra(EXTRA_OUTPUT_IS_NEW_DIARY, true)
                val diary: DiaryWriteContentUiModel? = intent.getParcelableExtra(EXTRA_OUTPUT_DIARY)

                return if (diary == null) {
                    null
                } else {
                    Output(
                        isNewDiary = isNewDiary,
                        diary = diary
                    )
                }
            }
        }
    }

    sealed class Input {
        data class Edit(
            val diaryId: Int,
            val originalDiary: DiaryWriteContentUiModel,
        ) : Input()

        object New : Input()
    }

    data class Output(
        val isNewDiary: Boolean,
        val diary: DiaryWriteContentUiModel
    )

    companion object {
        private const val EXTRA_INPUT_DIARY_ID = "diaryId"
        private const val EXTRA_INPUT_ORIGINAL_DIARY = "originalDiary"

        private const val EXTRA_OUTPUT_IS_NEW_DIARY = "isNewDiary"
        private const val EXTRA_OUTPUT_DIARY = "diary"

        private const val REQUEST_CODE_CAMERA = 0
    }
}