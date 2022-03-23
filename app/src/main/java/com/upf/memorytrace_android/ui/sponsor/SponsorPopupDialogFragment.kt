package com.upf.memorytrace_android.ui.sponsor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.upf.memorytrace_android.R
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

        arguments?.getBoolean(IS_COUNTING, true)?.let {
            if (it) {
                with(MemoryTraceConfig) {
                    lastShowSponsorPopupDate = Date()
                    sponsorPopupCount += 1
                }
            }
        }

        GaLogSender.sendEvent(
            GaLogSender.EVENT_SHOW_SPONSOR_POPUP, bundleOf(
                "show_sponsor_select_price_popup" to MemoryTraceConfig.sponsorPopupCount
            )
        )

        binding.buttonSponsor.setOnDebounceClickListener {
            viewModel.startBillingConnection()
        }

        repeatOnStart {
            launch {
                viewModel.uiState.map { it.isLoading }
                    .distinctCollect {
                        if (it) {
                            childFragmentManager.showAllowingStateLoss("loading") {
                                SponsorLoadingDialogFragment()
                            }
                        }
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
        }

        observeEvent(viewModel.billingState) { state ->
            when (state) {
                is BillingState.Done -> {
                    if (state.failedPurchase.isNotEmpty()) {
                        toast(getString(R.string.sponsor_error_fail_consume))
                    }
                    toast(getString(R.string.sponsor_thank_you))
                    dismissAllowingStateLoss()
                }
                is BillingState.ReadyToBilling -> {
                    val activity = activity ?: return@observeEvent
                    val responseCode =
                        state.billingClient.launchBillingFlow(
                            activity,
                            state.flowParams
                        ).responseCode
                    Log.d(javaClass.simpleName, "launchBillingFlow(): $responseCode")
                }
            }
        }

        observeEvent(viewModel.error) { error ->
            when (error) {
                is SponsorError.BillingError -> toast(error.message)
                is SponsorError.NoSkuDetailsError -> toast(getString(R.string.sponsor_error_no_sku))
                is SponsorError.BillingDisconnectError -> toast(getString(R.string.sponsor_error_disconnect))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkPurchases()
    }

    companion object {

        private const val IS_COUNTING = "isCounting"

        fun getInstance(isCounting: Boolean = true): SponsorPopupDialogFragment {
            return SponsorPopupDialogFragment().apply {
                arguments = bundleOf(IS_COUNTING to isCounting)
            }
        }
    }
}