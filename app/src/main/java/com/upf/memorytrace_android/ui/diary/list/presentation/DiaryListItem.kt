package com.upf.memorytrace_android.ui.diary.list.presentation

import com.upf.memorytrace_android.ui.diary.list.domain.Diary
import com.upf.memorytrace_android.util.TimeUtil
import java.util.*

sealed class DiaryListItem {
    data class DiaryItem(
        val diaryId: Int,
        val title: String,
        val nickname: String,
        val imageUrl: String,
        val createdDate: Date,
        val onItemClick: (diaryId: Int) -> Unit
    ) : DiaryListItem() {
        val createdDateString: String
            get() = TimeUtil.getDate(TimeUtil.YYYY_M_D_KR, createdDate)
    }

    data class DateItem(
        val date: Date
    ) : DiaryListItem()
}

fun Diary.toPresentEntity(onItemClick: (diaryId: Int) -> Unit) = DiaryListItem.DiaryItem(
    diaryId = diaryId,
    title = title,
    nickname = nickname,
    imageUrl = imageUrl,
    createdDate = createdDate,
    onItemClick = onItemClick
)

fun DiaryListItem.areItemsTheSame(newItem: DiaryListItem): Boolean = when (this) {
    is DiaryListItem.DiaryItem ->
        when (newItem) {
            is DiaryListItem.DiaryItem -> diaryId == newItem.diaryId
            is DiaryListItem.DateItem -> false
        }
    is DiaryListItem.DateItem ->
        when (newItem) {
            is DiaryListItem.DiaryItem -> false
            is DiaryListItem.DateItem -> date == newItem.date
        }
}

fun DiaryListItem.areContentsTheSame(newItem: DiaryListItem): Boolean = when (this) {
    is DiaryListItem.DiaryItem ->
        when (newItem) {
            is DiaryListItem.DiaryItem -> {
                diaryId == newItem.diaryId
                        && title == newItem.title
                        && nickname == newItem.nickname
                        && imageUrl == newItem.imageUrl
                        && createdDate == newItem.createdDate
            }
            is DiaryListItem.DateItem -> false
        }
    is DiaryListItem.DateItem ->
        when (newItem) {
            is DiaryListItem.DiaryItem -> false
            is DiaryListItem.DateItem -> date == newItem.date
        }
}