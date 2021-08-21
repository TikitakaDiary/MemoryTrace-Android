package com.upf.memorytrace_android.ui.book

import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.databinding.ItemBookListBinding
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.viewmodel.BookListViewModel

internal class BookViewHolder(
    val binding: ItemBookListBinding,
    val viewModel: BookListViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Book) {
        binding.itemBookAuthor.text =
            itemView.context.getString(R.string.book_list_author, item.nickname)
        binding.itemBookTitle.text = item.title
        Colors.fillColorTint(binding.itemBook, item.bgColor)

        itemView.setOnClickListener {
            viewModel.onClickDiary(item.bid)
        }
    }
}
