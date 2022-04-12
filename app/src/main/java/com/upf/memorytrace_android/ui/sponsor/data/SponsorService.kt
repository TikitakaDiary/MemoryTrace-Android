package com.upf.memorytrace_android.ui.sponsor.data

import com.upf.memorytrace_android.api.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SponsorService {

    @POST("/purchase")
    suspend fun postPurchaseToken(@Body purchaseTokenRequest: PurchaseTokenRequest): BaseResponse<*>
}