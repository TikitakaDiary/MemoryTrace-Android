package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.BaseResponse
import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.api.model.UserName
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.util.MemoryTraceConfig
import java.lang.Exception

object UserRepository {

    suspend fun createUser(nickname: String, token: String, snsType: String) {
        val user = User(nickname = nickname, snsKey = token, snsType = snsType)
        try {
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
        } catch (e: Exception) {

        }


    }

    suspend fun withdrawalUser() {
        try {
            val response = MemoryTraceUtils.apiService().withdrawalUser()
            if (response.isSuccess) {
                //todo;
            }
        } catch (e: Exception) {

        }

    }

    suspend fun editName(name: String) {
        try {
            val response = MemoryTraceUtils.apiService().editName(UserName(name))
        } catch (e: Exception) {

        }
    }

   
}