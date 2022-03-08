package com.upf.memorytrace_android.ui.diary.list.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ItemOtherTurnBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.SingleItemAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.PinchInfoUiState

class OtherTurnHeaderViewHolder(
    private val binding: ItemOtherTurnBinding,
    private val onPinchClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonOtherTurnPinch.setOnDebounceClickListener {
            onPinchClick.invoke()
        }
    }

    fun bind(pinchInfoUiState: PinchInfoUiState?) {
        val data = pinchInfoUiState ?: return

        binding.root.isVisible = data.isError.not().also { if (it) return }

        pinchInfoUiState.isLoading.run {
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
        fun createAdapter() =
            object : SingleItemAdapter<PinchInfoUiState, OtherTurnHeaderViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): OtherTurnHeaderViewHolder {
                    return OtherTurnHeaderViewHolder(
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

                override fun onBindViewHolder(holder: OtherTurnHeaderViewHolder, position: Int) {
                    holder.bind(getItem())
                }
            }

        private const val MAX_PINCH_COUNT = 3
    }
}