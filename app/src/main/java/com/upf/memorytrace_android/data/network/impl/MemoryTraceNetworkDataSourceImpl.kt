package com.upf.memorytrace_android.data.network.impl

import com.upf.memorytrace_android.data.network.MemoryTraceNetworkDataSource
import com.upf.memorytrace_android.data.network.MemoryTraceService
import com.upf.memorytrace_android.data.network.response.PostDiaryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
            bookId = bookId.toString().toPlainTextRequestBody(),
            title = title.toPlainTextRequestBody(),
            content = content.toPlainTextRequestBody(),
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
            diaryId = diaryId.toString().toPlainTextRequestBody(),
            title = title.toPlainTextRequestBody(),
            content = content.toPlainTextRequestBody(),
            imagePart = imageFile.toDiaryImageMultiPart()
        )
    }

    private fun String.toPlainTextRequestBody(): RequestBody {
        return toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun File.toDiaryImageMultiPart(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "img",
            name,
            asRequestBody("image/*".toMediaType())
        )
    }
}