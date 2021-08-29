package com.upf.memorytrace_android.api.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.upf.memorytrace_android.api.model.User
import com.upf.memorytrace_android.api.model.UserName
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.api.util.StatusError
import com.upf.memorytrace_android.util.MemoryTraceConfig
import java.lang.Exception

object UserRepository {
    private const val SUCCESS = "Success"
    private const val ERROR = "예상하지 못한 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."

    suspend fun createUser(
        nickname: String,
        snskey: String,
        snsType: String,
        token: String
    ): NetworkState<String> {
        return try {
            val user = User(nickname = nickname, snsKey = snskey, snsType = snsType, token = token)
            val response = MemoryTraceUtils.apiService().createUser(user)
            //update memory
            response.data?.let {
                MemoryTraceConfig.uid = it.uid
                MemoryTraceConfig.nickname = it.nickname
                MemoryTraceConfig.sns = it.snsType
                MemoryTraceConfig.profileImg = it.profileImg
                MemoryTraceConfig.token = it.jwt
                MemoryTraceConfig.signupDate = it.createdDate.substring(0, 10)

                MemoryTraceConfig.setCrashlyticsCustomKeys()
            }
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun withdrawalUser(): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().withdrawalUser()
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun editName(name: String): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().editName(UserName(name))
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun joinToBook(code: String): NetworkState<String> {
        return try {
            MemoryTraceUtils.apiService().joinToBook(code)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }

    suspend fun updateFcmToken(token: String): NetworkState<String> {
        return try {
            val user = User(uid = MemoryTraceConfig.uid, token = token)
            MemoryTraceUtils.apiService().registerFcmToken(user)
            NetworkState.Success(SUCCESS)
        } catch (e: StatusError) {
            NetworkState.Failure(e.responseMessage)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            NetworkState.Failure(ERROR)
        }
    }
}