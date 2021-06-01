package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.LiveEvent
import kotlinx.coroutines.launch
import java.lang.Exception

internal class CreateBookViewModel : BaseViewModel() {
    private var _selectedColor = MutableLiveData<Colors>()
    val selectedColor: LiveData<Colors>
        get() = _selectedColor

    //TODO: 개선할 것
    val createBookSuccess = LiveEvent<Unit?>()

    fun setSelectedColor(color: Colors) {
        _selectedColor.value = color
    }

    fun isSelected(color: Colors): Boolean {
        return _selectedColor.value == color
    }

    fun createBook(title: String) {
        viewModelScope.launch {
            try {
                val result = BookRepository.createBook(title, _selectedColor.value?.id ?: 0, "")
                createBookSuccess.call()
            } catch (e: Exception) {
                val error = e.message
                val error2 = e.cause
            }
        }
    }
//    private var _bookList: MutableLiveData<List<Book>> = MutableLiveData()
//    val bookList: LiveData<List<Book>>
//        get() = _bookList
//
//    private var page: Int = 1
//
//   fun init() {
//        viewModelScope.launch {
//            try {
//                val data = BookRepository.fetchBookList(1, page).data
//                if (data != null) {
//                    _bookList.postValue(data.bookList)
//                    if (data.hasNext) page++
//                }
//
//            } catch (e: Exception) {
//            }
//
//        }
//    }
}