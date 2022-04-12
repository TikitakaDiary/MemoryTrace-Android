package com.upf.memorytrace_android.ui.sponsor.data

import com.google.gson.annotations.SerializedName

data class PurchaseTokenRequest(
    @SerializedName("pcToken")
    val purchaseToken: String,
    val price: Int
)