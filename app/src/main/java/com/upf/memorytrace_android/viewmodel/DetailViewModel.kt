package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.DiaryDetailModel
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.DetailFragmentArgs
import com.upf.memorytrace_android.util.TimeUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class DetailViewModel : BaseViewModel() {
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val img = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            navArgs<DetailFragmentArgs>()
                .map {
                    val response = DiaryRepository.fetchDiary(it.did)
                    response.body()
                }.collect { res ->
                    res?.let { settingDiary(it.data) }
                }
        }
    }

    private fun settingDiary(data: DiaryDetailModel) {
        title.postValue(data.title)
        content.postValue(data.content)
        nickname.postValue(data.nickname)
        img.postValue(data.img)
        TimeUtil.convertStringToDate(TimeUtil.FORMAT_yyyy_MM_dd_B_HH_mm_ss, data.createdDate)?.let {
            date.postValue(TimeUtil.getDate(TimeUtil.YYYY_M_D_KR, it))
        }
    }
}