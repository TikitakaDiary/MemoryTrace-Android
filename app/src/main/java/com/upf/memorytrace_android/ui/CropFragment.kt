package com.upf.memorytrace_android.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.theartofdev.edmodo.cropper.CropImageView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentCropBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.viewmodel.CropViewModel

internal class CropFragment : BaseFragment<CropViewModel, FragmentCropBinding>(),
    CropImageView.OnCropImageCompleteListener {
    override val layoutId = R.layout.fragment_crop
    override val viewModelClass = CropViewModel::class
    override val navArgs by navArgs<CropFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCropView()
        observe(viewModel.isFinishCrop) { finishCrop() }
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropImageView.CropResult?) {
        result?.let {
            sendArgToBackStack("image", it.bitmap)
            viewModel.navDirections.value = BackDirections()
        }
    }

    private fun setCropView() {
        with(binding.cropIv) {
            if (navArgs.imageUri != null) setImageUriAsync(navArgs.imageUri)
            if (navArgs.bitmap != null) setImageBitmap(navArgs.bitmap)
            setOnCropImageCompleteListener(this@CropFragment)
        }
    }

    private fun finishCrop() {
        binding.cropIv.getCroppedImageAsync()
    }
}