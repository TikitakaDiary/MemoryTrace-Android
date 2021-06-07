package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.util.MemoryTraceConfig

object UserRepository {

    suspend fun createUser(nickname: String, token: String, snsType: String) {
        val user = User(nickname = nickname, snsKey = token, snsType = snsType)
        val response =
            MemoryTraceUtils.apiService().createUser(user)
        if (response.isSuccess) {
            //update memory
            response.data?.let {
                MemoryTraceConfig.nickname = it.nickname
                MemoryTraceConfig.token = it.jwt
                MemoryTraceConfig.sns = it.snsType
                MemoryTraceConfig.signupDate = it.createdDate.substring(0, 10)
            }
        }
    }

    suspend fun withdrawalUser() {
        val response = MemoryTraceUtils.apiService().withdrawalUser()
        if (response.isSuccess) {
            //todo; 
        }
    }
}