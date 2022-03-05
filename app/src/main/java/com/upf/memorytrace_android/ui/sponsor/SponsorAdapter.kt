package com.upf.memorytrace_android.ui.sponsor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.databinding.ItemSponsorBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener

class SponsorAdapter : ListAdapter<SponsorItemUiState, SponsorAdapter.SponsorViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SponsorViewHolder {
        return SponsorViewHolder(
            ItemSponsorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { getItem(it).onClick() }
    }

    override fun onBindViewHolder(holder: SponsorViewHolder, position: Int) {
        holder.binding.sponsorButton.text = getItem(position).price
    }

    class SponsorViewHolder(
        val binding: ItemSponsorBinding,
        val onClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnDebounceClickListener {
                onClick.invoke(bindingAdapterPosition)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<SponsorItemUiState>() {
            override fun areItemsTheSame(
                oldItem: SponsorItemUiState,
                newItem: SponsorItemUiState
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SponsorItemUiState,
                newItem: SponsorItemUiState
            ): Boolean {
                return oldItem.price == newItem.price
            }
        }
    }
}