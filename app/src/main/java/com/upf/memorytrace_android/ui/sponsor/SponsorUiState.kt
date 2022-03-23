package com.upf.memorytrace_android.ui.sponsor

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

data class SponsorUiState(
    val sponsorItems: List<SponsorItemUiState> = emptyList(),
    val isLoading: Boolean = false
)

data class SponsorItemUiState(
    val price: String = "",
    val onClick: () -> Unit = {}
)

fun SkuDetails.toUiState(onClick: () -> Unit) = SponsorItemUiState(price = price, onClick = onClick)

sealed class BillingState {
    data class ReadyToBilling(
        val billingClient: BillingClient,
        val flowParams: BillingFlowParams
    ): BillingState()

    data class Done(val failedPurchase: List<Purchase> = emptyList()): BillingState()
}

sealed class SponsorError {
    data class BillingError(val message: String): SponsorError()
    object NoSkuDetailsError: SponsorError()
    object BillingDisconnectError: SponsorError()
}