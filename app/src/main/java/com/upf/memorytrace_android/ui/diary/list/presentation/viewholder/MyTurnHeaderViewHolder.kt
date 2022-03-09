package com.upf.memorytrace_android.ui.diary.list.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.databinding.ItemMyTurnBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.SingleItemAdapter

class MyTurnHeaderViewHolder(
    binding: ItemMyTurnBinding,
    private val onHeaderClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnDebounceClickListener {
            onHeaderClick.invoke()
        }
    }

    companion object {
        fun createAdapter(
            onHeaderClick: () -> Unit
        ) = object: SingleItemAdapter<Unit, MyTurnHeaderViewHolder> (){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): MyTurnHeaderViewHolder {
                return MyTurnHeaderViewHolder(
                    ItemMyTurnBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onHeaderClick = onHeaderClick
                )
            }

            override fun onBindViewHolder(holder: MyTurnHeaderViewHolder, position: Int) {
                // Nothing to do
            }
        }
    }
}