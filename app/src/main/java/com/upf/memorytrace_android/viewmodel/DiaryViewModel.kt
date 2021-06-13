package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.diary.DiaryItem
import com.upf.memorytrace_android.ui.diary.DiaryFragmentDirections
import com.upf.memorytrace_android.ui.diary.DiaryFragmentArgs
import com.upf.memorytrace_android.ui.diary.DiaryListType
import com.upf.memorytrace_android.ui.diary.DiaryMonthItem
import com.upf.memorytrace_android.util.TimeUtil
import java.util.Calendar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class DiaryViewModel : BaseViewModel() {
    val title = MutableLiveData<String>()
    val listType = MutableLiveData<DiaryListType>(DiaryListType.FRAME)
    val diaryOfMonthList = MutableLiveData<List<DiaryMonthItem>>()
    val isMyTurn = true

    private var bid = -1

    init {
        viewModelScope.launch {
            navArgs<DiaryFragmentArgs>()
                .map {
                    bid = it.bid
                    val response = DiaryRepository.fetchDiaries(it.bid)
                    response.body()
                }.map {
                    title.value = it?.data?.title
                    it?.data
                        ?.diaryList
                        ?.map { diary ->
                            val date = TimeUtil.convertStringToDate(
                                TimeUtil.FORMAT_yyyy_MM_dd_B_HH_mm_ss,
                                diary.modifiedDate
                            ) ?: Calendar.getInstance().time
                            DiaryItem(
                                diary.id,
                                diary.title,
                                diary.nickname,
                                diary.img,
                                date
                            )
                        }?.groupBy { diaryItem ->
                            TimeUtil.getDate(
                                TimeUtil.YYYY_MM,
                                diaryItem.date
                            )
                        }?.map { entry ->
                            DiaryMonthItem(entry.key, entry.value)
                        }
                }.collect {
                    diaryOfMonthList.postValue(it)
                }
        }
    }

    fun onClickWriteDiary() {
        navDirections.value = DiaryFragmentDirections.actionDiaryFragmentToWriteFragment(bid)
    }

    fun changeListType() {
        listType.value = when (listType.value) {
            DiaryListType.FRAME -> DiaryListType.GRID
            DiaryListType.GRID -> DiaryListType.FRAME
            else -> DiaryListType.FRAME
        }
    }
}