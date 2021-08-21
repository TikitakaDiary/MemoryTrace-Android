package com.upf.memorytrace_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.upf.memorytrace_android.api.model.DiaryModel
import com.upf.memorytrace_android.api.repository.DiaryRepository
import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.ui.diary.DiaryItem
import com.upf.memorytrace_android.ui.diary.DiaryFragmentDirections
import com.upf.memorytrace_android.ui.diary.DiaryFragmentArgs
import com.upf.memorytrace_android.ui.diary.DiaryListType
import com.upf.memorytrace_android.ui.diary.DiaryMonthItem
import com.upf.memorytrace_android.util.BackDirections
import com.upf.memorytrace_android.util.MemoryTraceConfig
import com.upf.memorytrace_android.util.TimeUtil
import java.util.Calendar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class DiaryViewModel : BaseViewModel() {
    val title = MutableLiveData<String>()
    val listType = MutableLiveData<DiaryListType>(DiaryListType.FRAME)
    val diaryOfMonthList = MutableLiveData<List<DiaryMonthItem>>()
    val isLoading = MutableLiveData<Boolean>(false)
    val isMyTurn = MutableLiveData<Boolean>(false)

    private var bid = -1
    private var page = 1
    private var hasNext = true
    private var diaryModelList = mutableListOf<DiaryModel>()

    init {
        viewModelScope.launch {
            navArgs<DiaryFragmentArgs>()
                .collect {
                    bid = it.bid
                }
        }
    }

    fun initializeDiaryList() {
        diaryOfMonthList.value = listOf()
        diaryModelList = mutableListOf()
        page = 1
        hasNext = true
        loadDiaryList()
    }

    fun loadDiaryList() {
        if (hasNext && isLoading.value == false) {
            viewModelScope.launch {
                isLoading.postValue(true)
                val response = DiaryRepository.fetchDiaries(bid, page++)
                response.body()?.data?.apply {
                    this@DiaryViewModel.hasNext = hasNext
                    this@DiaryViewModel.title.value = title
                    isMyTurn.postValue(MemoryTraceConfig.uid == whoseTurn)
                    diaryModelList.addAll(diaryList)
                    val diaryList = diaryModelList
                        .map { diary ->
                            val date = TimeUtil.convertStringToDate(
                                TimeUtil.FORMAT_yyyy_MM_dd_B_HH_mm_ss,
                                diary.createdDate
                            ) ?: Calendar.getInstance().time
                            DiaryItem(
                                diary.id,
                                diary.title,
                                diary.nickname,
                                diary.img,
                                date
                            )
                        }.groupBy { diaryItem ->
                            TimeUtil.getDate(
                                TimeUtil.YYYY_MM,
                                diaryItem.date
                            )
                        }.map { entry ->
                            DiaryMonthItem(entry.key, entry.value)
                        }
                    diaryOfMonthList.postValue(diaryList)
                }
                isLoading.postValue(false)
            }
        }
    }

    fun onClickWriteDiary() {
        navDirections.value = DiaryFragmentDirections.actionDiaryFragmentToWriteFragment(bid)
    }

    fun onClickDiaryDetail(did: Int) {
        navDirections.value = DiaryFragmentDirections.actionDiaryFragmentToDetailFragment(did)
    }

    fun onclickSetting() {
        navDirections.value = DiaryFragmentDirections.actionDiaryFragmentToBookSettingFragment(bid)
    }

    fun onClickBack() {
        navDirections.value = BackDirections()
    }

    fun changeListType() {
        listType.value = when (listType.value) {
            DiaryListType.FRAME -> DiaryListType.GRID
            DiaryListType.GRID -> DiaryListType.FRAME
            else -> DiaryListType.FRAME
        }
    }
}