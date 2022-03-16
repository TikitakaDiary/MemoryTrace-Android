package com.upf.memorytrace_android.ui.diary.list.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ItemMyTurnBinding
import com.upf.memorytrace_android.databinding.ItemOtherTurnBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.diary.list.presentation.DiaryHeaderUiModel

abstract class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    abstract fun bind(diaryHeaderUiModel: DiaryHeaderUiModel?)

    class MyTurnHeaderViewHolder(
        binding: ItemMyTurnBinding,
        private val onHeaderClick: () -> Unit
    ) : HeaderViewHolder(binding.root) {

        init {
            binding.root.setOnDebounceClickListener {
                onHeaderClick.invoke()
            }
        }

        override fun bind(diaryHeaderUiModel: DiaryHeaderUiModel?) { }
    }

    class OtherTurnHeaderViewHolder(
        private val binding: ItemOtherTurnBinding,
        private val onPinchClick: () -> Unit
    ) : HeaderViewHolder(binding.root) {

        init {
            binding.buttonOtherTurnPinch.setOnDebounceClickListener {
                onPinchClick.invoke()
            }
        }

        override fun bind(diaryHeaderUiModel: DiaryHeaderUiModel?) {
            val data = diaryHeaderUiModel ?: return
            if (data.isError) return

            diaryHeaderUiModel.isLoading.run {
                binding.rootOtherTurn.isInvisible = this
                binding.progressbarOtherTurn.isVisible = this
            }

            binding.titleOtherTurn.text =
                binding.root.context.getString(R.string.header_other_turn_title, data.turnUserName)
            binding.linearlayoutPinchCount.renderPinchCount(data.pinchCount)
            binding.buttonOtherTurnPinch.isEnabled = data.pinchCount > 0
        }

        private fun LinearLayout.renderPinchCount(pinchCount: Int) {
            removeAllViews()
            for (i in 0 until MAX_PINCH_COUNT) {
                val duckImageView = LayoutInflater.from(context)
                    .inflate(R.layout.item_pinch_duck, binding.linearlayoutPinchCount, false)
                if (i >= pinchCount) {
                    duckImageView.alpha = 0.5f
                }
                addView(duckImageView)
            }
        }

        companion object {
            private const val MAX_PINCH_COUNT = 3
        }
    }
}