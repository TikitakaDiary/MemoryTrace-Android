package com.upf.memorytrace_android.ui.diary.comment.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ItemCommentBinding
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment

class CommentListAdapter(
    private val onReplyClick: (comment: Comment) -> Unit
): ListAdapter<Comment, CommentListAdapter.CommentViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(parent).apply {
            binding?.commentReplyButton?.setOnClickListener {
                onReplyClick.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding?.comment = getItem(position)
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.commentId == newItem.commentId
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.content == newItem.content
                        && oldItem.createdDate == newItem.createdDate
                        && oldItem.isDelete == newItem.isDelete
            }
        }
    }

    inner class CommentViewHolder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
    ) {
        val binding = DataBindingUtil.bind<ItemCommentBinding>(itemView)
    }
}