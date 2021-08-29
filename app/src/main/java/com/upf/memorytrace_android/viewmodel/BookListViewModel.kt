package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.util.CollectionUtils
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.book.BookListFragmentDirections
import kotlinx.coroutines.launch
import java.util.*

internal class BookListViewModel : BaseViewModel() {
    private var _bookList: MutableLiveData<List<Book>> = MutableLiveData()
    val bookList: LiveData<List<Book>>
        get() = _bookList

    private var page: Int = 1
    private var hasNext = true
    val isLoading = MutableLiveData<Boolean>(false)

    fun init() {
        page = 1
        hasNext = true
        _bookList.value = mutableListOf()
        fetchBooks()
    }

    fun fetchBooks() {
        if (hasNext && isLoading.value == false) {
            viewModelScope.launch {
                isLoading.postValue(true)
                val response = BookRepository.fetchBookList(page++)
                when (response) {
                    is NetworkState.Success -> {
                        response.data.let {
                            val list = _bookList.value as ArrayList<Book>
                            list.addAll(it.bookList ?: emptyList())
                            _bookList.postValue(list)
                            hasNext = it.hasNext
                        }

                    }
                    is NetworkState.Failure -> toast.value = response.message
                }
                isLoading.postValue(false)
            }
        }

    }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if ((firstVisibleItem + visibleItemCount) >= totalItemCount - BookRepository.PAGE_SIZE) {
            fetchBooks()
        }
    }

    fun onClickCreateBook() {
        navDirections.value =
            BookListFragmentDirections.actionBookListFragmentToCreateBookFragment()
    }

    fun onClickMypage() {
        navDirections.value =
            BookListFragmentDirections.actionBookListFragmentToMypageFragment()
    }

    fun onClickDiary(did: Int) {
        navDirections.value = BookListFragmentDirections.actionBookListFragmentToDiaryFragment(did)
    }
}