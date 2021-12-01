package com.upf.memorytrace_android.ui.diary.comment.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.ui.diary.comment.domain.FetchCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val fetchCommentsUseCase: FetchCommentUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Comment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Comment>>> = _uiState

    fun fetchComments(diaryId: Int) {
        viewModelScope.launch {
            _uiState.emit(fetchCommentsUseCase(diaryId))
        }
    }
}