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
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.color.toColorInt
import com.upf.memorytrace_android.databinding.ActivityDiaryWriteBinding
import com.upf.memorytrace_android.databinding.LayoutDiaryWriteSelectColorContainerBinding
import com.upf.memorytrace_android.extension.*
import com.upf.memorytrace_android.sticker.SelectStickerDialogFragment
import com.upf.memorytrace_android.ui.diary.write.color.ColorAdapter
import com.upf.memorytrace_android.ui.diary.write.image.ImageCropActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.upf.memorytrace_android.util.showDialog
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.StickerView
import kotlinx.parcelize.Parcelize
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

        val newInput: Input.New? = intent.getParcelableExtra(EXTRA_INPUT_NEW)
        val editInput: Input.Edit? = intent.getParcelableExtra(EXTRA_INPUT_EDIT)

        repeatOnStart {
            launch {
                viewModel.contentUiModel.collect {
                    binding.writeTitle.setTextIfNew(it.title)
                    binding.writeContents.setTextIfNew(it.content)
                    binding.writeWriter.text = it.writerName
                    binding.writeDate.text = it.date
                    binding.setPolaroidImage(it.image)

                    val hasDiff = it.stickerEdited || viewModel.hasDiaryDiff()
                    binding.writeToolbarButton.isEnabled = hasDiff && it.canPost
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
                                binding.writeStickerContentsImage.removeStickerHandler()
                                val file = binding.writeStickerContentsImage.toDiaryImageFile()
                                viewModel.postDiary(file)
                            }
                        }
                        DiaryWriteToolbarState.EDIT -> {
                            binding.writeToolbarTitle.text = ""
                            binding.setBackButtonCancel()
                            binding.setToolbarButton(R.string.write_save) {
                                binding.writeStickerContentsImage.removeStickerHandler()
                                val file = binding.writeStickerContentsImage.toDiaryImageFile()
                                viewModel.editedDiary(file)
                            }
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
                is DiaryWriteEvent.FinishWriteActivity -> {
                    finish()
                }
                is DiaryWriteEvent.ShowFinishConfirmDialog -> {
                    showDialog(
                        context = this,
                        title = R.string.write_exit_title,
                        message = R.string.write_exit_content,
                        confirm = R.string.write_exit_exit,
                        positive = {
                            viewModel.clearBackUpData()
                            finish()
                        }
                    )
                }
                is DiaryWriteEvent.PostDone -> {
                    val intent = Intent().apply {
                        putExtra(EXTRA_OUTPUT_IS_NEW_DIARY, true)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
                is DiaryWriteEvent.EditDone -> {
                    val intent = Intent().apply {
                        putExtra(EXTRA_OUTPUT_IS_NEW_DIARY, false)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
                is DiaryWriteEvent.AddSticker -> {
                    val drawableSticker = DrawableSticker(event.stickerDrawable)
                    binding.writeStickerContentsImage.addSticker(drawableSticker)
                }
            }
        }

        observeEvent(viewModel.errorEvent) { event ->
            when (event) {
                is DiaryWriteErrorEvent.WrongAccess -> {
                    toast(R.string.wrong_access)
                    setResult(RESULT_CANCELED)
                    finish()
                }
                is DiaryWriteErrorEvent.FailPost -> {
                    toast(event.message ?: getString(R.string.write_failure_post))
                }
            }
        }
        binding.writeSelectImageButton.setOnDebounceClickListener {
            showSelectImageTypeDialogFragment()
        }
        binding.writeNoContentsImage.setOnDebounceClickListener {
            showSelectImageTypeDialogFragment()
        }
        binding.writeSelectStickerButton.setOnDebounceClickListener {
            if (binding.writeNoContentsImage.isVisible) {
                toast(R.string.write_sticker_alert_no_content)
            } else {
                showSelectStickerDialogFragment(
                    onStickerClick = { imageRes ->
                        val drawable = ContextCompat.getDrawable(this, imageRes)
                        if (drawable != null) {
                            viewModel.onStickerItemSelected(drawable)
                        }
                    }
                )
            }
        }
        binding.writeTitle.doAfterTextChanged {
            viewModel.onTitleChanged(it?.toString().orEmpty())
        }
        binding.writeContents.doAfterTextChanged {
            viewModel.onContentChanged(it?.toString().orEmpty())
        }

        when {
            editInput != null -> {
                viewModel.loadOriginalDiary(editInput.diaryId, editInput.originalDiary)
            }
            newInput != null -> {
                viewModel.setBookId(newInput.bookId)
            }
            else -> {
                toast(R.string.wrong_access)
                setResult(RESULT_CANCELED)
                finish()
            }
        }
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
        viewModel.onBackPressed()
    }

    private fun showSelectStickerDialogFragment(onStickerClick: (Int) -> Unit) {
        showAllowingStateLoss("selectSticker") {
            SelectStickerDialogFragment().apply {
                setOnStickClick(onStickerClick)
            }
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
                viewModel.onBackPressed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonCancel() {
        useTextBackButton {
            it.text = getString(R.string.write_exit_cancel)
            it.setOnDebounceClickListener {
                viewModel.onBackPressed()
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
                    writeSelectColorToolbarCancel.setOnDebounceClickListener {
                        viewModel.dismissSelectColorLayout()
                    }
                    writeSelectColorToolbarSave.setOnDebounceClickListener {
                        viewModel.onSaveSelectColorClick()
                    }
                }
        }
        selectColorBinding?.apply(apply)
    }

    private fun Uri.cropImage() {
        imageCropActivityResultLauncher.launch(this)
    }

    private fun StickerView.toDiaryImageFile(): File {
        return toBitmap().toFile(
            context = this@DiaryWriteActivity,
            fileName = DIARY_IMAGE_FILE_NAME,
            childDirectoryName = DIARY_IMAGE_DIRECTORY_NAME
        )
    }

    class DiaryWriteContract : ActivityResultContract<Input, Output?>() {
        override fun createIntent(context: Context, input: Input): Intent {
            return Intent(context, DiaryWriteActivity::class.java).apply {
                if (input is Input.Edit) {
                    putExtra(EXTRA_INPUT_EDIT, input)
                } else if (input is Input.New) {
                    putExtra(EXTRA_INPUT_NEW, input)
                }
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Output? {
            return if (intent == null || resultCode != RESULT_OK) {
                null
            } else {
                val isNewDiary = intent.getBooleanExtra(EXTRA_OUTPUT_IS_NEW_DIARY, true)
                val diaryId: Int? =
                    intent.getIntExtra(EXTRA_OUTPUT_DIARY_ID, -1).takeIf { it != -1 }

                return if (diaryId == null) {
                    null
                } else {
                    Output(
                        isNewDiary = isNewDiary,
                        diaryId = diaryId
                    )
                }
            }
        }
    }

    sealed class Input : Parcelable {
        @Parcelize
        data class Edit(
            val diaryId: Int,
            val originalDiary: DiaryWriteContentUiModel,
        ) : Input()

        @Parcelize
        data class New(
            val bookId: Int
        ) : Input()
    }

    data class Output(
        val isNewDiary: Boolean,
        val diaryId: Int
    )

    companion object {
        private const val EXTRA_INPUT_EDIT = "inputEdit"
        private const val EXTRA_INPUT_NEW = "inputNew"

        private const val EXTRA_OUTPUT_IS_NEW_DIARY = "isNewDiary"
        private const val EXTRA_OUTPUT_DIARY_ID = "diaryId"

        private const val REQUEST_CODE_CAMERA = 0

        private const val DIARY_IMAGE_FILE_NAME = "diary-image"
        private const val DIARY_IMAGE_DIRECTORY_NAME = "diary"
    }
}