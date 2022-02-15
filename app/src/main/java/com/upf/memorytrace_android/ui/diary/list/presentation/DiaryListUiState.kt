package com.upf.memorytrace_android.ui.diary.list.presentation

import com.upf.memorytrace_android.ui.diary.list.DiaryListType

data class DiaryListUiState(
    val bookId: Int = -1,
    val title: String = "",
    val listType: DiaryListType = DiaryListType.LINEAR,
    val diaries: List<DiaryListItem> = listOf(),
    val page: Int = 0,
    val hasNext: Boolean = true,
    val isMyTurn: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String = ""
) {
    fun getPositionsOfDiaryDateItem(): Set<Int> {
        val positions = mutableSetOf<Int>()
        diaries.forEachIndexed { index, diaryListItem ->
            if (diaryListItem is DiaryListItem.DateItem) {
                positions.add(index)
            }
        }
        return positions
    }
}