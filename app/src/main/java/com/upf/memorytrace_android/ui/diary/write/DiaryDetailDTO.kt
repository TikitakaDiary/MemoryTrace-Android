package com.upf.memorytrace_android.ui.diary.write

import android.os.Parcelable
import androidx.annotation.Keep
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
class DiaryDetailDTO(
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
