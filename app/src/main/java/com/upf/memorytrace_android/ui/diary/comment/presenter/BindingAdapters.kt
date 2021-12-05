package com.upf.memorytrace_android.ui.diary.comment.presenter

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("noCommentVisible")
    fun View.setNoCommentVisible(uiState: UiState<List<Comment>>) {
        isVisible = uiState is UiState.Success && uiState.data.isEmpty()
    }

    @JvmStatic
    @BindingAdapter("commentItems")
    fun RecyclerView.setCommentItems(uiState: UiState<List<Comment>>) {
        if (uiState !is UiState.Success) return
        isVisible = uiState.data.isNotEmpty()

        val commentListAdapter = adapter
        if (commentListAdapter is CommentListAdapter) {
            commentListAdapter.submitList(uiState.data)
        }
    }

    @JvmStatic
    @BindingAdapter("replyingModeText")
    fun TextView.setReplyingModeText(comment: Comment?) {
        comment?.let { text = String.format("%s님께 댓글 남기는 중", it.nickname) }
    }
}