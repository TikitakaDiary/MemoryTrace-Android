package com.upf.memorytrace_android.ui.sponsor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SponsorViewModel @Inject constructor(
    billingClientBuilder: BillingClient.Builder
) : ViewModel(), PurchasesUpdatedListener {

    private val _uiState = MutableStateFlow(SponsorUiState())
    val uiState: StateFlow<SponsorUiState>
        get() = _uiState

    private val _billingState = MutableStateFlow<BillingState>(BillingState.None)
    val billingState: StateFlow<BillingState>
        get() = _billingState

    private val billingClient = billingClientBuilder
        .setListener(this)
        .enablePendingPurchases()
        .build()

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.isSuccess() && purchases != null) {
            viewModelScope.launch {
                val consumeResults = mutableMapOf<Purchase, Boolean>()
                for (purchase in purchases) {
                    consumeResults[purchase] = handlePurchase(purchase)
                }
                _billingState.update {
                    BillingState.Done(consumeResults.filter { it.value.not() }.keys.toList())
                }
            }
        } else {
            Log.e(javaClass.simpleName, "onPurchasesUpdated(): ${billingResult.debugMessage}")
            _billingState.update { BillingState.Failure(billingResult.responseCode) }
        }
    }

    fun startBillingConnection() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.isFailure()) {
                    Log.e(javaClass.simpleName, "billing setup is finished(): ${billingResult.debugMessage}")
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                    return
                }
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sponsorItems = getSkuDetailList()
                                .sortedBy { skuDetails ->
                                    skuDetails.price.replace(NUMBER_REGEX, "").toInt()
                                }
                                .map { skuDetails ->
                                    skuDetails.toUiState(onClick = { doSponsor(skuDetails) })
                                }
                        )
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e(javaClass.simpleName, "billing service is disconnected")
            }
        })
    }

    private suspend fun handlePurchase(purchase: Purchase): Boolean {
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        val consumeResult = withContext(Dispatchers.IO) {
            billingClient.consumePurchase(consumeParams)
        }
        return consumeResult.billingResult.isSuccess()
    }

    fun doSponsor(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        _billingState.update { BillingState.ReadyToBilling(billingClient, flowParams) }
    }

    private suspend fun getSkuDetailList(): List<SkuDetails> {
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(SKU_LIST)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        val result = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params)
        }
        if (result.billingResult.isFailure()) {
            Log.e(javaClass.simpleName, "getSkuDetails(): ${result.billingResult.debugMessage}")
            return emptyList()
        }
        val skuDetailList = result.skuDetailsList
        if (skuDetailList == null) {
            Log.d(javaClass.simpleName, "getSkuDetails(): No SkuDetails for $SKU_LIST")
            return emptyList()
        }
        for (skuDetail in skuDetailList) {
            Log.d(javaClass.simpleName, "getSkuDetails() : $skuDetail,\n${skuDetail.originalJson}")
        }
        return skuDetailList
    }

    fun clearSponsorItems() {
        _uiState.update {
            it.copy(sponsorItems = emptyList())
        }
    }

    companion object {
        private val SKU_LIST = listOf("sponsor_900", "sponsor_1900", "sponsor_3900")
        private val NUMBER_REGEX = Regex("[^0-9]")
    }

    private fun BillingResult.isSuccess() = responseCode == BillingClient.BillingResponseCode.OK
    private fun BillingResult.isFailure() = responseCode != BillingClient.BillingResponseCode.OK
}