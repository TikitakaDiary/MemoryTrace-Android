package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.util.MemoryTraceConfig

object UserRepository {

    suspend fun createUser(nickname: String, token: String, snsType: String) {
        val response =
            MemoryTraceUtils.apiService().createUser(nickname, token,snsType)
        if (response.isSuccess) {
            //update memory
            MemoryTraceConfig.nickname = response.data?.nickname
            MemoryTraceConfig.token = response.data?.jwt
        }
    }
}