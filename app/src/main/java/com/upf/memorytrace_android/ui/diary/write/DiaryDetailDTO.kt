package com.upf.memorytrace_android.ui.diary.write

import android.os.Parcelable
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiaryDetailDTO(
    val diaryId: Int,
    val title: String,
    val content: String,
    val imageUrl: String
): Parcelable {

    companion object {
        fun from(diaryDetail: DiaryDetail) = DiaryDetailDTO(
            diaryDetail.diaryId, diaryDetail.title, diaryDetail.content, diaryDetail.imageUrl
        )
    }
}
