package com.upf.memorytrace_android.ui.book.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentBookListBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.showAllowingStateLoss
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.ui.sponsor.ui.SponsorPopupDialogFragment
import com.upf.memorytrace_android.util.MemoryTraceConfig

internal class BookListFragment : BaseFragment<BookListViewModel, FragmentBookListBinding>() {
    override val layoutId = R.layout.fragment_book_list
    override val viewModelClass = BookListViewModel::class
    private val bookAdapter = BookListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatas()
        setupRecyclerView()
        initViewModel()
        viewModel.checkSponsorPopupPeriod()
    }

    private fun initDatas() {
        binding.title.text = getString(R.string.book_list_title, MemoryTraceConfig.nickname)
    }

    private fun initViewModel() {
        observe(viewModel.bookList) { bookList ->
            bookAdapter.submitList(bookList.map { BookItem(it) })
        }
        observeEvent(viewModel.uiEvent) {
            when (it) {
                BookListViewModel.Event.ShowSponsorPopup -> {
                    childFragmentManager.showAllowingStateLoss("sponsor") {
                        SponsorPopupDialogFragment.getInstance()
                    }
                }
            }
        }
        viewModel.init()
    }

    private fun setupRecyclerView() {
        bookAdapter.setViewHolderViewModel(viewModel)
        binding.list.apply {
            adapter = bookAdapter
            addOnScrollListener(onScrollChangeListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.list.removeOnScrollListener(onScrollChangeListener)
    }

    private val onScrollChangeListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerView.adapter?.itemCount ?: 0
            val firstVisibleItem =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

            viewModel.loadMore(visibleItemCount, totalItemCount, firstVisibleItem)
        }
    }
}