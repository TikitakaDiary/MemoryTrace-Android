package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.book.BookListFragmentDirections
import kotlinx.coroutines.launch

internal class BookListViewModel : BaseViewModel() {
    private var _bookList: MutableLiveData<List<Book>> = MutableLiveData()
    val bookList: LiveData<List<Book>>
        get() = _bookList

    private var page: Int = 1

    init {
        viewModelScope.launch {
            try {
                val data = BookRepository.fetchBookList(page).data
                if (data != null) {
                    _bookList.postValue(data.bookList)
                    if (data.hasNext) page++
                }

            } catch (e: Exception) {
                //TODO: error handling
            }

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

}