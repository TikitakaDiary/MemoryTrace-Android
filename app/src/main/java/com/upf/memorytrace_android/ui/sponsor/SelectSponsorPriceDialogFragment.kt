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
import com.upf.memorytrace_android.databinding.DialogSelectSponsorPriceBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.firebase.GaLogSender
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSponsorPriceDialogFragment : DialogFragment() {

    private val viewModel: SponsorViewModel by viewModels()

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

        binding.buttonPrice1.setOnDebounceClickListener { viewModel.onClickPrice900() }
        binding.buttonPrice2.setOnDebounceClickListener { viewModel.onClickPrice1900() }
        binding.buttonPrice3.setOnDebounceClickListener { viewModel.onClickPrice3900() }
    }
}