package com.upf.memorytrace_android.ui.diary.comment.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.ui.diary.comment.domain.FetchCommentUseCase
import com.upf.memorytrace_android.ui.diary.comment.domain.PostCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val fetchCommentsUseCase: FetchCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase
) : ViewModel() {

    private var diaryId: Int? = null

    private val _uiState = MutableStateFlow<UiState<List<Comment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Comment>>> = _uiState

    private val _currentReplying = MutableLiveData<Comment?>()
    val currentReplying: LiveData<Comment?> = _currentReplying

    val currentCommentText = MutableLiveData<String>()

    fun fetchComments(diaryId: Int) {
        this.diaryId = diaryId
        viewModelScope.launch {
            _uiState.emit(fetchCommentsUseCase(diaryId))
        }
    }

    fun startReplyingMode(comment: Comment) {
        _currentReplying.value = comment
    }

    fun stopReplyingMode() {
        _currentReplying.value = null
    }

    fun postComment() {
        viewModelScope.launch {
            if (diaryId != null && currentCommentText.value != null) {
                _uiState.emit(UiState.Loading)

                val parentCommentId = currentReplying.value?.commentId
                val content = currentCommentText.value
                currentCommentText.value = null
                _currentReplying.value = null

                _uiState.emit(
                    postCommentUseCase(
                        parentCommentId,
                        diaryId!!,
                        content!!
                    )
                )
            }
        }
    }
}