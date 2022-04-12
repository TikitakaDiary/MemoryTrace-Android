package com.upf.memorytrace_android.api.model

import com.google.gson.*
import java.lang.reflect.Type

open class BaseResponse<out T>(
    val statusCode: String = "",
    val responseMessage: String? = "",
    val data: T? = null
) {
    val isSuccess: Boolean
        get() = statusCode == "201" || statusCode == "200"

    class Deserializer : JsonDeserializer<BaseResponse<*>> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): BaseResponse<*> {
            val jsonObject = json.asJsonObject
            return Gson().fromJson(jsonObject, BaseResponse::class.java)
        }
    }
}