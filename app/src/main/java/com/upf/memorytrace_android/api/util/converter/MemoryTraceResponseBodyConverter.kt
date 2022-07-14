package com.upf.memorytrace_android.api.util.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import com.upf.memorytrace_android.api.model.BaseResponse
import com.upf.memorytrace_android.api.util.StatusError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException

class MemoryTraceResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
): Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val jsonReader = gson.newJsonReader(value.charStream())
        try {
            val result: BaseResponse<T> = gson.fromJson(jsonReader, BaseResponse::class.java)
            if (result.isSuccess) {
                val data = gson.toJson(result.data)
                return adapter.fromJson(data)
            } else {
                throw StatusError(result)
            }
        } catch (e: Exception) {
            if (e is StatusError) {
                throw e
            } else {
                throw IOException(e)
            }
        } finally {
            value.close()
        }
    }
}