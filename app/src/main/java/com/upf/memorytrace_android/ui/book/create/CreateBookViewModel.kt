package com.upf.memorytrace_android.ui.book.create

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.api.repository.BookRepository
import com.upf.memorytrace_android.api.util.NetworkState
import com.upf.memorytrace_android.ui.base.BaseViewModel
import com.upf.memorytrace_android.util.*
import com.upf.memorytrace_android.util.ImageConverter
import com.upf.memorytrace_android.util.LiveEvent
import com.xiaopo.flying.sticker.Sticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

internal class CreateBookViewModel : BaseViewModel() {
    val stickerImg = MutableLiveData<String?>()
    val title = MutableLiveData<String>()

    val showStickerResetDialog = MutableLiveData<Boolean?>()
    val isShowStickerDialog = LiveEvent<Unit?>()
    val isHideStickerDialog = LiveEvent<Unit?>()
    val addSticker = LiveEvent<Int>()
    val isSaveBook = LiveEvent<Unit?>()

    private var _selectedColor = MutableLiveData<Colors>()
    val selectedColor: LiveData<Colors>
        get() = _selectedColor

    fun setSelectedColor(color: Colors) {
        _selectedColor.value = color
    }

    fun isSelected(color: Colors): Boolean {
        return _selectedColor.value == color
    }

    var bid = -1
    var stickerList = mutableListOf<Sticker>()

    init {
        viewModelScope.launch {
            navArgs<CreateBookFragmentArgs>()
                .map {
                    bid = it.bid
                }.collect {
                    if (bid > 0) {
                        val response = BookRepository.fetchBook(bid)
                        when (response) {
                            is NetworkState.Success -> settingBook(response.data)
                            is NetworkState.Failure -> toast.value = response.message
                        }
                    }
                }
        }
    }

    private fun settingBook(book: Book?) {
        book?.let {
            bid = it.bid
            title.value = it.title
            stickerImg.value = it.image
            setSelectedColor(Colors.getColor(it.bgColor))
        }
    }

    fun showStickerDialog(force: Boolean) {
        if (bid < 0 || showStickerResetDialog.value == false) {
            isShowStickerDialog.call()
        } else {
            if (force) {
                showStickerResetDialog.value = false
                stickerImg.value = null
                isShowStickerDialog.call()
            } else {
                showStickerResetDialog.value = true
            }
        }
    }

    fun hideStickerDialog() {
        isHideStickerDialog.call()
    }

    fun attachSticker(@DrawableRes stickerId: Int) {
        addSticker.value = stickerId
    }

    fun saveBook() {
        isSaveBook.call()
    }

    fun onClickBack() {
        navDirections.postValue(BackDirections())
    }

    fun createBook(cacheDir: File, bitmap: Bitmap?) {
        viewModelScope.launch {
            if (title.value.isNullOrBlank()) {
                toast.value = EMPTY_TITLE
            } else {
                var image: MultipartBody.Part? = null
                if (bitmap != null) {
                    ImageConverter.convertBitmapToMultipartBody(bitmap, cacheDir, null, "stickerImg")?.let {
                        image = it
                    }
                }

                val response = if (bid < 0) {
                    BookRepository.createBook(
                        title.value ?: EMPTY_STRING,
                        _selectedColor.value?.id ?: 0,
                        image
                    )
                } else {
                    BookRepository.updateBook(
                        bid,
                        title.value ?: EMPTY_STRING,
                        _selectedColor.value?.id ?: 0,
                        image
                    )
                }
                when (response) {
                    is NetworkState.Success -> onClickBack()
                    is NetworkState.Failure -> toast.value = response.message
                }
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_TITLE = "????????? ??????????????????!"
    }
}