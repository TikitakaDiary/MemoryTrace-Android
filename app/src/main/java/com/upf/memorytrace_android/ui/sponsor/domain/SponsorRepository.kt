package com.upf.memorytrace_android.ui.sponsor.domain

import com.upf.memorytrace_android.api.util.NetworkState

interface SponsorRepository {

    suspend fun postPurchaseToken(token: String, price: Int): NetworkState<Unit>
}