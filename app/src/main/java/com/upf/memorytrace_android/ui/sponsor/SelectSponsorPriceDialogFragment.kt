package com.upf.memorytrace_android.ui.sponsor

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.upf.memorytrace_android.databinding.DialogSelectSponsorPriceBinding
import com.upf.memorytrace_android.extension.distinctCollect
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.firebase.GaLogSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectSponsorPriceDialogFragment : DialogFragment() {

    private val viewModel: SponsorViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogSelectSponsorPriceBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSelectSponsorPriceBinding.bind(view)

        GaLogSender.sendEvent(GaLogSender.EVENT_SHOW_SPONSOR_SELECT_PRICE_POPUP)

        val adapter = SponsorAdapter { dismissAllowingStateLoss() }
        binding.recyclerviewSponsorItems.adapter = adapter

        repeatOnStart {
            launch {
                viewModel.uiState.map { it.sponsorItems }
                    .distinctCollect {
                        if (it.isEmpty()) {
                            dismiss()
                            return@distinctCollect
                        }
                        adapter.submitList(it)
                    }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.clearSponsorItems()
    }
}