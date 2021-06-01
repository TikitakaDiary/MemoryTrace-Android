package com.upf.memorytrace_android.api.util

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newBuilder = original.newBuilder()

        // TODO: preference로 관리하도록 수정 필요
        val isAuthorizedAccount: Boolean = true
        val jwt: String =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZXkxIiwiaWF0IjoxNjIxMzQ4NDk0fQ.0n9rRThJzxgw20LgeoFj_MtICdU17YtGHqXkbbWLhAI"

        if (!isAuthorizedAccount) {
            throw Exception("oauth is not started.")
        }

        newBuilder.header("Authorization", jwt)

        return chain.proceed(newBuilder.build())
    }

}