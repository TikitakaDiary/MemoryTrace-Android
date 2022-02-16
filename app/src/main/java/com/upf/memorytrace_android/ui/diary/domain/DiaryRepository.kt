package com.upf.memorytrace_android.ui.diary.domain

import com.upf.memorytrace_android.ApiResult
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.ui.diary.list.domain.DiaryList

interface DiaryRepository {
    suspend fun fetchDiary(diaryId: Int): DiaryDetail

    suspend fun fetchDiaries(bookId: Int, page: Int, size: Int): ApiResult<DiaryList>
}