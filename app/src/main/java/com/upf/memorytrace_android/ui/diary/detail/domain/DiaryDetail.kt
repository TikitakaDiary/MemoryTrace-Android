package com.upf.memorytrace_android.ui.diary.detail.domain

import android.net.Uri
import com.upf.memorytrace_android.ui.diary.write.DiaryWriteActivity
import com.upf.memorytrace_android.ui.diary.write.DiaryWriteContentUiModel
import com.upf.memorytrace_android.ui.diary.write.WriteImageType

data class DiaryDetail(
    val isModifiable: Boolean,
    val diaryId: Int,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrl: String,
    val date: String,
    val isMine: Boolean,
    val commentCount: Int
)

fun DiaryDetail.toDiaryWriteInput(): DiaryWriteActivity.Input {
    return DiaryWriteActivity.Input.Edit(
        diaryId = diaryId,
        originalDiary = DiaryWriteContentUiModel(
            title = title,
            content = content,
            date = date,
            writerName = nickname,
            image = WriteImageType.Image(Uri.parse(imageUrl))
        )
    )
}