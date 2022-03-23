package com.upf.memorytrace_android.ui.sponsor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
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
) : ViewModel(), PurchasesUpdatedListener, PurchasesResponseListener {

    private val _uiState = MutableStateFlow(SponsorUiState())
    val uiState: StateFlow<SponsorUiState>
        get() = _uiState

    private val _billingEvent = MutableEventLiveData<BillingState>()
    val billingState: EventLiveData<BillingState>
        get() = _billingEvent

    private val _error = MutableEventLiveData<SponsorError>()
    val error: EventLiveData<SponsorError>
        get() = _error

    private val billingClient = billingClientBuilder
        .setListener(this)
        .enablePendingPurchases()
        .build()

    fun startBillingConnection() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.isFailure()) {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    _error.event = SponsorError.BillingError(billingResult.debugMessage)
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
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _error.event = SponsorError.BillingDisconnectError
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
        _billingEvent.event = BillingState.ReadyToBilling(billingClient, flowParams)
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
            _error.event = SponsorError.BillingError(result.billingResult.debugMessage)
            return emptyList()
        }
        val skuDetailList = result.skuDetailsList
        if (skuDetailList == null) {
            _error.event = SponsorError.NoSkuDetailsError
            return emptyList()
        }
        return skuDetailList
    }

    fun clearSponsorItems() {
        _uiState.update {
            it.copy(sponsorItems = emptyList())
        }
    }

    fun checkPurchases() {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, this)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        _uiState.update { it.copy(isLoading = true) }
        consumePurchases(billingResult, purchases)
    }

    override fun onQueryPurchasesResponse(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>
    ) {
        if (purchases.isEmpty()) return
        _uiState.update { it.copy(isLoading = true) }
        consumePurchases(billingResult, purchases)
    }

    private fun consumePurchases(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.isSuccess() && purchases != null) {
            viewModelScope.launch {
                val consumeResults = mutableMapOf<Purchase, Boolean>()
                for (purchase in purchases) {
                    consumeResults[purchase] = handlePurchase(purchase)
                }
                _billingEvent.event =
                    BillingState.Done(consumeResults.filter { it.value.not() }.keys.toList())
                _uiState.update { it.copy(isLoading = false) }
            }
        } else {
            Log.e(javaClass.simpleName, "onPurchasesUpdated(): ${billingResult.debugMessage}")
            _error.event = SponsorError.BillingError(billingResult.debugMessage)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun BillingResult.isSuccess() = responseCode == BillingClient.BillingResponseCode.OK
    private fun BillingResult.isFailure() = responseCode != BillingClient.BillingResponseCode.OK

    companion object {
        private val SKU_LIST = listOf("sponsor_900", "sponsor_1900", "sponsor_3900")
        private val NUMBER_REGEX = Regex("[^0-9]")
    }
}