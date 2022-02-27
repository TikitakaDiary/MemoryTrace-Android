package com.upf.memorytrace_android.ui.sponsor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.upf.memorytrace_android.databinding.DialogSponsorPopupBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.extension.showAllowingStateLoss
import com.upf.memorytrace_android.firebase.GaLogSender
import com.upf.memorytrace_android.util.MemoryTraceConfig
import java.util.*

class SponsorPopupDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogSponsorPopupBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSponsorPopupBinding.bind(view)

        with(MemoryTraceConfig) {
            lastShowSponsorPopupDate = Date()
            sponsorPopupCount += 1
        }

        GaLogSender.sendEvent(GaLogSender.EVENT_SHOW_SPONSOR_POPUP, bundleOf(
            "count" to MemoryTraceConfig.sponsorPopupCount
        ))

        binding.buttonSponsor.setOnDebounceClickListener {
            childFragmentManager.showAllowingStateLoss("select_price") {
                SelectSponsorPriceDialogFragment()
            }
        }
    }
}