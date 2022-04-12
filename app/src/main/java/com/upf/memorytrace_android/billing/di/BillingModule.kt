package com.upf.memorytrace_android.billing.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class BillingModule {

    @Provides
    fun provideBillingBuilder(@ApplicationContext context: Context): BillingClient.Builder =
        BillingClient.newBuilder(context)
}