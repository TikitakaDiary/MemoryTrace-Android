package com.upf.memorytrace_android.ui.book.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.databinding.EventLiveData
import com.upf.memorytrace_android.databinding.MutableEventLiveData
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.TimeUtil
import kotlinx.coroutines.launch
import java.util.*

internal class BookListViewModel : BaseViewModel() {

    enum class Event {
        ShowSponsorPopup
    }

    private var _bookList: MutableLiveData<List<Book>> = MutableLiveData()
    val bookList: LiveData<List<Book>>
        get() = _bookList

    private var page: Int = 1
    private var hasNext = true
    val isLoading = MutableLiveData<Boolean>(false)

    private val _uiEvent = MutableEventLiveData<Event>()
    val uiEvent: EventLiveData<Event>
        get() = _uiEvent

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

    fun checkSponsorPopupPeriod() {
        val lastShowSponsorPopupDate = MemoryTraceConfig.lastShowSponsorPopupDate
        if (lastShowSponsorPopupDate == null) {
            MemoryTraceConfig.lastShowSponsorPopupDate = Date()
            return
        }
        val diff = TimeUtil.getDayDiffAbs(Date(), lastShowSponsorPopupDate)
        if (diff >= PERIOD_SPONSOR_POPUP) {
            showSponsorPopup()
        }
    }

    fun showSponsorPopup() {
        _uiEvent.event = Event.ShowSponsorPopup
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

    companion object {
        private const val PERIOD_SPONSOR_POPUP = 28
    }
}