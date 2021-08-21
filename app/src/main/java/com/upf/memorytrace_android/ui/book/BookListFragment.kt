package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            //todo: 생성후 넘어왔을때를 위해 추가했으며, 페이징 추가하면 수정해야함.
            if (bookList.isNotEmpty())
                binding.list.smoothScrollToPosition(0)
        }
        viewModel.init()
    }

    private fun setupRecyclerView() {
        adapter.setViewHolderViewModel(viewModel)
        binding.list.adapter = adapter
    }
}