package com.upf.memorytrace_android.ui.diary.list.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.upf.memorytrace_android.databinding.ItemMyTurnBinding
import com.upf.memorytrace_android.databinding.ItemOtherTurnBinding
import com.upf.memorytrace_android.ui.SingleItemAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryHeaderUiModel
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.HeaderViewHolder

class DiaryHeaderAdapter() : SingleItemAdapter<DiaryHeaderUiModel, HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return if (viewType == VIEW_TYPE_MY_TURN) {
            HeaderViewHolder.MyTurnHeaderViewHolder(
                ItemMyTurnBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onHeaderClick = {
                    getItem()?.onMyTurnClick?.invoke()
                }
            )
        } else {
            HeaderViewHolder.OtherTurnHeaderViewHolder(
                ItemOtherTurnBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onPinchClick = {
                    getItem()?.onPinchClick?.invoke()
                }
            )
        }
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(getItem())
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem() ?: return VIEW_TYPE_DEFAULT
        return if (item.isMyTurn) {
            VIEW_TYPE_MY_TURN
        } else {
            VIEW_TYPE_OTHER_TURN
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_TURN = 0
        private const val VIEW_TYPE_OTHER_TURN = 1
        private const val VIEW_TYPE_DEFAULT = VIEW_TYPE_OTHER_TURN
    }
}