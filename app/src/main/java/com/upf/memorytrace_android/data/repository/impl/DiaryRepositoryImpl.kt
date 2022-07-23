package com.upf.memorytrace_android.data.repository.impl

import com.upf.memorytrace_android.api.ApiResponse
import com.upf.memorytrace_android.api.map
import com.upf.memorytrace_android.api.toApiResponse
import com.upf.memorytrace_android.data.network.MemoryTraceNetworkDataSource
import com.upf.memorytrace_android.data.repository.DiaryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

class DiaryRepositoryImpl(
    private val network: MemoryTraceNetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher,
): DiaryRepository {

    override suspend fun postDiary(
        bookId: Int,
        title: String,
        content: String,
        imageFile: File
    ): ApiResponse<Int> {
        return withContext(ioDispatcher) {
            network.postDiary(
                bookId = bookId,
                title = title,
                content = content,
                imageFile = imageFile
            ).toApiResponse().map { it.diaryId }
        }
    }

    override suspend fun editDiary(
        diaryId: Int,
        title: String,
        content: String,
        imageFile: File
    ): ApiResponse<Int> {
        return withContext(ioDispatcher) {
            network.editDiary(
                diaryId = diaryId,
                title = title,
                content = content,
                imageFile = imageFile
            ).toApiResponse().map { it.diaryId }
        }
    }
}