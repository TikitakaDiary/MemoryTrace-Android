package com.upf.memorytrace_android.api.repository

import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.api.model.UserName
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.util.MemoryTraceConfig
import java.lang.Exception

object UserRepository {

    suspend fun createUser(
        nickname: String,
        snskey: String,
        snsType: String,
        token: String
    ): NetworkState<String> {
        return try {
            val user = User(nickname = nickname, snsKey = snskey, snsType = snsType, token = token)
            val response =
                MemoryTraceUtils.apiService().createUser(user)
            if (response.isSuccess) {
                //update memory
                response.data?.let {
                    MemoryTraceConfig.uid = it.uid
                    MemoryTraceConfig.nickname = it.nickname
                    MemoryTraceConfig.sns = it.snsType
                    MemoryTraceConfig.profileImg = it.profileImg
                    MemoryTraceConfig.token = it.jwt
                    MemoryTraceConfig.signupDate = it.createdDate.substring(0, 10)
                }
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun withdrawalUser(): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().withdrawalUser()
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun editName(name: String): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().editName(UserName(name))
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun joinToBook(code: String): NetworkState<String> {
        return try {
            val response = MemoryTraceUtils.apiService().joinToBook(code)
            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }

    suspend fun updateFcmToken(token: String): NetworkState<String> {
        return try {
            val user = User(uid = MemoryTraceConfig.uid, token = token)
            val response = MemoryTraceUtils.apiService().registerFcmToken(user)

            if (response.isSuccess) {
                NetworkState.Success("Success")
            } else {
                NetworkState.Failure(response.responseMessage)
            }
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "Internet Error")
        }
    }
}