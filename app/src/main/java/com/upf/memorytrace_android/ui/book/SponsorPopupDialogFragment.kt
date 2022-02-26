package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.upf.memorytrace_android.databinding.DialogSponsorPopupBinding
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

        with(MemoryTraceConfig) {
            lastShowSponsorPopupDate = Date()
            sponsorPopupCount += 1
        }

        GaLogSender.sendEvent(GaLogSender.GA_EVENT_SHOW_SPONSOR_POPUP, bundleOf(
            "count" to MemoryTraceConfig.sponsorPopupCount
        ))
    }
}