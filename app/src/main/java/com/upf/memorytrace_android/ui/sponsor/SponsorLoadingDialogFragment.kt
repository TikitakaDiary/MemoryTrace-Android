package com.upf.memorytrace_android.ui.sponsor

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.upf.memorytrace_android.databinding.DialogLoadingBinding
import com.upf.memorytrace_android.extension.distinctCollect
import com.upf.memorytrace_android.extension.repeatOnStart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SponsorLoadingDialogFragment : DialogFragment() {

    private val viewModel: SponsorViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogLoadingBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repeatOnStart {
            launch {
                viewModel.uiState.map { it.isLoading }
                    .distinctCollect { isLoading ->
                        if (isLoading.not()) {
                            dismissAllowingStateLoss()
                        }
                    }
            }
        }
    }
}