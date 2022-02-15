package com.upf.memorytrace_android.ui.diary.list.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.databinding.ItemOtherTurnBinding
import com.upf.memorytrace_android.ui.SingleItemAdapter

class OtherTurnHeaderViewHolder(
    private val binding: ItemOtherTurnBinding,
    private val onUrgingClick: () -> Unit
): RecyclerView.ViewHolder(binding.root){

    fun bind() {
        // Todo: 다른 사람의 턴 안내 및 재촉하기 기능
    }

    companion object {
        fun createAdapter(
            onUrgingClick: () -> Unit
        ) = object: SingleItemAdapter<OtherTurnHeaderViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): OtherTurnHeaderViewHolder {
                return OtherTurnHeaderViewHolder(
                    ItemOtherTurnBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onUrgingClick = onUrgingClick
                )
            }

            override fun onBindViewHolder(holder: OtherTurnHeaderViewHolder, position: Int) {
                holder.bind()
            }
        }
    }
}