package com.upf.memorytrace_android.ui.sponsor.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import com.upf.memorytrace_android.ui.sponsor.domain.SponsorRepository
import javax.inject.Inject

class SponsorRepositoryImpl @Inject constructor(
    private val sponsorService: SponsorService
): SponsorRepository {

    override suspend fun postPurchaseToken(token: String, price: Int): NetworkState<Unit> {
        return try {
            sponsorService.postPurchaseToken(PurchaseTokenRequest(token, price))
            NetworkState.Success(Unit)
        } catch (e: StatusError) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure("")
        }
    }
}