package com.upf.memorytrace_android.api.util.interceptor

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.GsonBuilder
import com.upf.memorytrace_android.api.model.BaseResponse
import com.upf.memorytrace_android.api.util.StatusError
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class StatusInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        //todo: 더 좋은 방법을 찾아볼 것(https://github.com/square/okhttp/issues/1240#issuecomment-330813274)
        val body = response.peekBody(Long.MAX_VALUE)
        val gson = GsonBuilder().registerTypeAdapter(
            BaseResponse::class.java,
            BaseResponse.Deserializer()
        ).create()
        val data: BaseResponse<*> = gson.fromJson(body.string(), BaseResponse::class.java)
        if (!data.isSuccess) {
            FirebaseCrashlytics.getInstance().log(data.responseMessage)
            FirebaseCrashlytics.getInstance().recordException(StatusError(data))
            throw StatusError(data)
        }
        return response
    }

}