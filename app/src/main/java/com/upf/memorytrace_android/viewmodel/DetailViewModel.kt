package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.base.BaseViewModel

internal class DetailViewModel : BaseViewModel() {
    val img = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()
}