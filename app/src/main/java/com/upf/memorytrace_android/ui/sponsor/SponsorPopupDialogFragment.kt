package com.upf.memorytrace_android.ui.sponsor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.upf.memorytrace_android.databinding.DialogSponsorPopupBinding
import com.upf.memorytrace_android.extension.*
import com.upf.memorytrace_android.firebase.GaLogSender
import com.upf.memorytrace_android.util.MemoryTraceConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SponsorPopupDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: SponsorViewModel by viewModels()

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

        GaLogSender.sendEvent(
            GaLogSender.EVENT_SHOW_SPONSOR_POPUP, bundleOf(
                "count" to MemoryTraceConfig.sponsorPopupCount
            )
        )

        binding.buttonSponsor.setOnDebounceClickListener {
            viewModel.startBillingConnection()
        }

        repeatOnStart {
            launch {
                viewModel.uiState.map { it.isLoading }
                    .distinctCollect {
                        //Todo: 프로그래스 다이얼로그 띄우기
                    }
            }
            launch {
                viewModel.uiState.map { it.sponsorItems }
                    .distinctCollect {
                        if (it.isNotEmpty()) {
                            childFragmentManager.showAllowingStateLoss("select_price") {
                                SelectSponsorPriceDialogFragment()
                            }
                        }
                    }
            }
            launch {
                viewModel.uiState.map { it.event }
                    .distinctCollect {
                        //Todo: 에러 표시
                    }
            }
            launch {
                viewModel.billingState
                    .distinctCollect {
                        if (it is BillingState.Done) {
                            dismissAllowingStateLoss()
                        }
                    }
            }
        }
    }
}