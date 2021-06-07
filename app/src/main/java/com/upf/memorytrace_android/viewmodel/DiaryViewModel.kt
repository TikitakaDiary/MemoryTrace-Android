package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.diary.DiaryItem
import com.upf.memorytrace_android.ui.diary.DiaryFragmentDirections
import com.upf.memorytrace_android.ui.diary.DiaryListItem
import com.upf.memorytrace_android.ui.diary.DiaryListType
import com.upf.memorytrace_android.ui.diary.DiaryMonthItem
import com.upf.memorytrace_android.util.TimeUtil
import java.util.Calendar

internal class DiaryViewModel : BaseViewModel() {
    val diaryListItem = MutableLiveData<List<DiaryListItem>>()

    val title = MutableLiveData<String>()
    val listType = MutableLiveData<DiaryListType>(DiaryListType.FRAME)
    val diaryOfMonthList = MutableLiveData<List<DiaryMonthItem>>()
    val isMyTurn = true

    init {
        mutableListOf<DiaryListItem>().apply {
            if (isMyTurn) add(DiaryListItem.WriteButtonItem())
            diaryListItem.value = this
        }

        diaryOfMonthList.value = (1..11).map {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -(it / 2))
            val date = calendar.time
            DiaryItem(it, "title$it", "n$it", "", date)
        }.groupBy { TimeUtil.getDate(TimeUtil.YYYY_MM, it.date) }
            .map {
                DiaryMonthItem(it.key, it.value)
            }
    }

    fun onClickWriteDiary() {
        navDirections.value = DiaryFragmentDirections.actionDiaryFragmentToWriteFragment()
    }

    fun changeListType() {
        listType.value = when (listType.value) {
            DiaryListType.FRAME -> DiaryListType.GRID
            DiaryListType.GRID -> DiaryListType.FRAME
            else -> DiaryListType.FRAME
        }
    }
}