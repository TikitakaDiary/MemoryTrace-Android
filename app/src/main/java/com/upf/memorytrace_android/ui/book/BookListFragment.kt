package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentBookListBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.viewmodel.BookListViewModel

internal class BookListFragment : BaseFragment<BookListViewModel, FragmentBookListBinding>() {
    override val layoutId = R.layout.fragment_book_list
    override val viewModelClass = BookListViewModel::class
    private val adapter = BookListAdapter()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDatas()
        setupRecyclerView()
        initViewModel()
    }

    private fun initDatas() {
        binding.title.text = getString(R.string.book_list_title, MemoryTraceConfig.nickname)
    }

    private fun initViewModel() {
        observe(viewModel.bookList) { bookList ->
            adapter.submitList(bookList.map { BookItem(it) })
        }
    }

    private fun setupRecyclerView() {
        adapter.setViewHolderViewModel(viewModel)
        binding.list.adapter = adapter
    }

}