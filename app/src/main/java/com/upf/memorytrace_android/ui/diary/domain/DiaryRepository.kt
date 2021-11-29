package com.upf.memorytrace_android.ui.diary.domain

import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail

interface DiaryRepository {
    suspend fun fetchDiary(diaryId: Int): DiaryDetail
}