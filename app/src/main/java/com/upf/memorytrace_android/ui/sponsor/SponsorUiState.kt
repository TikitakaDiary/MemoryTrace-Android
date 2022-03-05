package com.upf.memorytrace_android.ui.sponsor

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

data class SponsorUiState(
    val sponsorItems: List<SponsorItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val event: SponsorError? = null,
)

data class SponsorItemUiState(
    val price: String = "",
    val onClick: () -> Unit = {}
)

enum class SponsorError {

}

fun SkuDetails.toUiState(onClick: () -> Unit) = SponsorItemUiState(price = price, onClick = onClick)

sealed class BillingState {
    object None: BillingState()

    data class ReadyToBilling(
        val billingClient: BillingClient,
        val flowParams: BillingFlowParams
    ): BillingState()

    data class Done(val failedPurchase: List<Purchase> = emptyList()): BillingState()
    data class Failure(val statusCode: Int): BillingState()
}