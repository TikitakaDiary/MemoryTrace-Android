package com.upf.memorytrace_android.api.util.interceptor

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.GsonBuilder
import com.upf.memorytrace_android.api.model.BaseResponse
import com.upf.memorytrace_android.api.util.StatusError
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class StatusInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        //todo: 더 좋은 방법을 찾아볼 것(https://github.com/square/okhttp/issues/1240#issuecomment-330813274)
        val body = response.peekBody(Long.MAX_VALUE)
        val gson = try {
            GsonBuilder().registerTypeAdapter(
                BaseResponse::class.java,
                BaseResponse.Deserializer()
            ).create()
        } catch (e: Exception) {
            val logMessage = "url : ${request.url}, body : ${request.body}, responseBody : $body"
            FirebaseCrashlytics.getInstance().run {
                log(logMessage)
                recordException(e)
            }
            return response
        }
        val data: BaseResponse<*> = gson.fromJson(body.string(), BaseResponse::class.java)
        if (!data.isSuccess) {
            val logMessage = "${data.responseMessage}, url : ${request.url}, body : ${request.body}"
            FirebaseCrashlytics.getInstance().run {
                log(logMessage)
                recordException(StatusError(data))
            }
            if (data.responseMessage.isNullOrEmpty()) throw IOException()
            throw StatusError(data)
        }
        return response
    }
}
