package com.upf.memorytrace_android.ui.diary.list.presentation

data class DiaryListUiState(
    val title: String = "",
    val isLoading: Boolean = true,
    val listContents: DiaryListContentUiState = DiaryListContentUiState()
)

data class DiaryListContentUiState(
    val listType: DiaryListType = DiaryListType.LINEAR,
    val diaries: List<DiaryListItem> = listOf(),
    val isMyTurn: Boolean = false,
    val isForce: Boolean = false
)

data class PinchInfoUiState(
    val isLoading: Boolean = true,
    val turnUserName: String = "",
    val pinchCount: Int = 0,
    val onPinchClick: () -> Unit = {},
    val isError: Boolean = false
)