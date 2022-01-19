package com.upf.memorytrace_android.ui.diary.data

import com.upf.memorytrace_android.ApiResult
import com.upf.memorytrace_android.api.util.MemoryTraceUtils
import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.data.remote.toEntity
import com.upf.memorytrace_android.ui.diary.data.remote.toEntry
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRepository {

    override suspend fun fetchDiary(diaryId: Int): DiaryDetail {
        return withContext(Dispatchers.IO) {
            diaryService.fetchDiary(diaryId).data!!.toEntry(MemoryTraceConfig.uid)!!
        }
    }

    override suspend fun fetchDiaries(bookId: Int, page: Int, size: Int): ApiResult<DiaryList> {
        return withContext(Dispatchers.IO) {
            try {
                ApiResult.Success(
                    MemoryTraceUtils.apiService().fetchDiaries(bookId, page, size).data!!.toEntity()
                )
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }
}