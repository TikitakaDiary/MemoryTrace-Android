package com.upf.memorytrace_android.data.network.impl

import com.upf.memorytrace_android.data.network.MemoryTraceNetworkDataSource
import com.upf.memorytrace_android.data.network.MemoryTraceService
import com.upf.memorytrace_android.data.network.response.PostDiaryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File

class MemoryTraceNetworkDataSourceImpl(
    private val service: MemoryTraceService
): MemoryTraceNetworkDataSource {

    override fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        imageFile: File
    ): Call<PostDiaryResponse> {
        return service.postDiary(
            bookId = bookId,
            title = title,
            content = content,
            imagePart = imageFile.toDiaryImageMultiPart()
        )
    }

    override fun editDiary(
        diaryId: Int,
        title: String,
        content: String,
        imageFile: File
    ): Call<Unit> {
        return service.editDiary(
            diaryId = diaryId,
            title = title,
            content = content,
            imagePart = imageFile.toDiaryImageMultiPart()
        )
    }

    private fun File.toDiaryImageMultiPart(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "img",
            name,
            asRequestBody("image/*".toMediaType())
        )
    }
}