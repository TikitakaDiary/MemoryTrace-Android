package com.upf.memorytrace_android.ui.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.base.BaseListAdapter
import com.upf.memorytrace_android.databinding.ItemBookListBinding
import com.upf.memorytrace_android.util.clearAndAddAll

class BookListAdapter : RecyclerView.Adapter<BookViewHolder>() {

    private val items = mutableListOf<Book>()

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: List<Book>) {
        //TODO: diffUtil이 제대로 작동안해서? 우선 notifyDataSetChanged처리 해놓음 ..
        // 뒤로가기시 최 상단이동, diffutil처리 확인 필요
        val result = DiffUtil.calculateDiff(ItemDiffCallback(items, newItems))
        items.clearAndAddAll(newItems)
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }


    class ItemDiffCallback(
        private val oldList: List<Book>,
        private val newList: List<Book>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id
                    && oldItem.nickname == newItem.nickname
                    && oldItem.title == newItem.title
                    && oldItem.bgColor == newItem.bgColor
                    && oldItem.date == newItem.date
                    && oldItem.image == newItem.image
        }
    }

}