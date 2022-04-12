package com.upf.memorytrace_android.ui.diary.list.presentation

data class DiaryListUiState(
    val title: String = "",
    val isLoading: Boolean = false
)

data class DiaryListTypeUiModel(
    val listType: DiaryListType = DiaryListType.LINEAR,
    val scrollPosition: Int = 0
)

data class DiaryListContentUiModel(
    val diaries: List<DiaryListItem> = listOf(),
    val isMyTurn: Boolean = false,
    val isForce: Boolean = false,
)

data class DiaryHeaderUiModel(
    val isMyTurn: Boolean = false,
    val isLoading: Boolean = true,
    val turnUserName: String = "",
    val pinchable: Boolean = false,
    val pinchCount: Int = 0,
    val onPinchClick: () -> Unit = {},
    val onMyTurnClick: () -> Unit = {},
    val isError: Boolean = false
)