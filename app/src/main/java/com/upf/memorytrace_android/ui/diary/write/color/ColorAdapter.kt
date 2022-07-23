package com.upf.memorytrace_android.ui.diary.write.color

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.color.UserColor
import com.upf.memorytrace_android.color.toColorInt
import com.upf.memorytrace_android.databinding.ItemColorBinding
import com.upf.memorytrace_android.ui.BindingAdapters.setOnDebounceClickListener

class ColorAdapter : ListAdapter<ColorItemUiModel, ColorAdapter.ColorViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ColorViewHolder(binding).apply {
            binding.root.setOnDebounceClickListener {
                getItem(bindingAdapterPosition).onSelectColorItem.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.itemColor.background.setTint(item.color.toColorInt())
        holder.binding.itemColorSelectedBorder.isVisible = item.isSelected
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ColorItemUiModel>() {
            override fun areItemsTheSame(
                oldItem: ColorItemUiModel,
                newItem: ColorItemUiModel
            ): Boolean {
                return oldItem.color == newItem.color
            }

            override fun areContentsTheSame(
                oldItem: ColorItemUiModel,
                newItem: ColorItemUiModel
            ): Boolean {
                return oldItem.color == newItem.color
                        && oldItem.isSelected == newItem.isSelected
            }
        }
    }

    class ColorViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root)
}