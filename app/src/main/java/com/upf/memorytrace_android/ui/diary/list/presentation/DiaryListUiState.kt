package com.upf.memorytrace_android.ui.diary.list.presentation

data class DiaryListUiState(
    val bookId: Int = -1,
    val title: String = "",
    val listType: DiaryListType = DiaryListType.LINEAR,
    val diaries: List<DiaryListItem> = listOf(),
    val page: Int = 0,
    val hasNext: Boolean = true,
    val isMyTurn: Boolean = false,
    val isLoading: Boolean = true,
    val isForce: Boolean = false,
    val isFailure: Boolean = false,
    val errorMessage: String = ""
)