package com.upf.memorytrace_android.ui.diary.data

import com.upf.memorytrace_android.ui.diary.data.remote.DiaryService
import com.upf.memorytrace_android.ui.diary.data.remote.toEntry
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.domain.DiaryRepository
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryService: DiaryService
): DiaryRepository {

    override suspend fun fetchDiary(diaryId: Int): DiaryDetail {
        return withContext(Dispatchers.IO) {
            diaryService.fetchDiary(diaryId).data!!.toEntry(MemoryTraceConfig.uid)!!
        }
    }
}