package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentBookListBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.viewmodel.BookListViewModel

internal class BookListFragment : BaseFragment<BookListViewModel, FragmentBookListBinding>() {
    override val layoutId = R.layout.fragment_book_list
    override val viewModelClass = BookListViewModel::class
    private val adapter: BookListAdapter by lazy {
        BookListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        observe(viewModel.bookList) {
            adapter.updateItems(it)
        }
    }

    private fun setupRecyclerView() {
        binding.list.setHasFixedSize(true)
        binding.list.adapter = adapter
    }

}