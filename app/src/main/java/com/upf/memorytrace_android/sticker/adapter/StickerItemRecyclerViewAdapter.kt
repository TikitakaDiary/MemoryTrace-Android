package com.upf.memorytrace_android.sticker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.databinding.ItemStickerBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.sticker.model.StickerItemUiModel

class StickerItemRecyclerViewAdapter :
    RecyclerView.Adapter<StickerItemRecyclerViewAdapter.StickerItemViewHolder>() {

    private val stickers = mutableListOf<StickerItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerItemViewHolder {
        return StickerItemViewHolder(
            binding = ItemStickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = { position ->
                stickers.getOrNull(position)?.onClick?.invoke()
            }
        )
    }

    override fun onBindViewHolder(holder: StickerItemViewHolder, position: Int) {
        stickers.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return stickers.size
    }

    fun submitList(stickers: List<StickerItemUiModel>) {
        this.stickers.clear()
        this.stickers.addAll(stickers)
        notifyDataSetChanged()
    }

    class StickerItemViewHolder(
        val binding: ItemStickerBinding,
        private val onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnDebounceClickListener {
                onItemClick.invoke(bindingAdapterPosition)
            }
        }

        fun bind(sticker: StickerItemUiModel) {
            binding.root.setImageResource(sticker.imageResource)
        }
    }
}