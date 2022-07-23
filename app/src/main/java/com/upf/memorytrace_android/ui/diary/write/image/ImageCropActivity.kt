package com.upf.memorytrace_android.ui.diary.write.image

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImageView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ActivityImageCropBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.extension.toUri
import com.upf.memorytrace_android.extension.toast
import java.text.SimpleDateFormat
import java.util.*

class ImageCropActivity : AppCompatActivity(), CropImageView.OnCropImageCompleteListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityImageCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri: Uri? = intent.getParcelableExtra(EXTRA_INPUT_IMAGE_URI)
        if (imageUri == null) {
            toast(getString(R.string.wrong_access))
            setResult(RESULT_CANCELED)
            finish()
        } else {
            with (binding.cropImageView) {
                setImageUriAsync(imageUri)
                setOnCropImageCompleteListener(this@ImageCropActivity)
            }
        }

        binding.cropImageOk.setOnDebounceClickListener {
            binding.cropImageView.getCroppedImageAsync()
        }

        binding.cropImageBack.setOnDebounceClickListener {
            onBackPressed()
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        runCatching {
            result.bitmap.toUri(
                context = this,
                fileName = getCroppedImageFileName(),
                childDirectoryName = CROPPED_IMAGE_DIRECTORY_NAME
            )
        }.onSuccess { uri ->
            val outputIntent = Intent().apply {
                putExtra(EXTRA_OUTPUT_CROPPED_IMAGE_URI, uri)
            }
            setResult(RESULT_OK, outputIntent)
            finish()
        }.onFailure {
            Log.w("ImageCropActivity", it)
            toast(getString(R.string.wrong_access))
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onBackPressed() {
        com.upf.memorytrace_android.util.showDialog(
            context = this,
            title = R.string.crop_image_cancel_dialog_title,
            message = R.string.crop_image_cancel_dialog_content,
            confirm = R.string.crop_image_cancel_dialog_ok,
            positive = {
                setResult(RESULT_CANCELED)
                finish()
            }
        )
    }

    fun getCroppedImageFileName(): String = SimpleDateFormat(
        "yyyyMMdd_HHmmss", Locale.KOREA
    ).format(Date()) + ".jpg"

    class ImageCropContract: ActivityResultContract<Uri, Uri?>() {
        override fun createIntent(context: Context, input: Uri?): Intent {
            return Intent(context, ImageCropActivity::class.java).apply {
                putExtra(EXTRA_INPUT_IMAGE_URI, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return if (intent == null || resultCode != RESULT_OK) {
                null
            } else {
                return intent.getParcelableExtra(EXTRA_OUTPUT_CROPPED_IMAGE_URI)
            }
        }
    }

    companion object {
        private const val EXTRA_INPUT_IMAGE_URI = "imageUri"
        private const val EXTRA_OUTPUT_CROPPED_IMAGE_URI = "croppedImageUri"

        private const val CROPPED_IMAGE_DIRECTORY_NAME = "cropped"
    }
}