package com.upf.memorytrace_android.ui.sponsor.domain

import com.upf.memorytrace_android.api.util.NetworkState
import javax.inject.Inject

class PostPurchaseTokenUseCase @Inject constructor(
    private val sponsorRepository: SponsorRepository
) {

    suspend operator fun invoke(token: String, price: Int): NetworkState<Unit> {
        return sponsorRepository.postPurchaseToken(token, price)
    }
}