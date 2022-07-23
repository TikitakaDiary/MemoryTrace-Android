package com.upf.memorytrace_android.sticker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.databinding.ItemStickerPackageBinding
import com.upf.memorytrace_android.sticker.model.StickerPackageUiModel

class StickerPackageRecyclerViewAdapter :
    RecyclerView.Adapter<StickerPackageRecyclerViewAdapter.StickerPackageViewHolder>() {

    private val packages = mutableListOf<StickerPackageUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerPackageViewHolder {
        return StickerPackageViewHolder(
            binding = ItemStickerPackageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StickerPackageViewHolder, position: Int) {
        packages.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return packages.size
    }

    fun submitList(packages: List<StickerPackageUiModel>) {
        this.packages.clear()
        this.packages.addAll(packages)
        notifyDataSetChanged()
    }

    class StickerPackageViewHolder(
        val binding: ItemStickerPackageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.adapter = StickerItemRecyclerViewAdapter()
        }

        fun bind(stickerPackage: StickerPackageUiModel) {
            val adapter = binding.root.adapter as? StickerItemRecyclerViewAdapter
            adapter?.submitList(stickers = stickerPackage.stickers)
        }
    }
}