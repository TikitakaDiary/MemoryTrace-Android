package com.upf.memorytrace_android.viewmodel

import com.upf.memorytrace_android.base.BaseViewModel
import com.upf.memorytrace_android.util.BackDirections

internal class MemberSettingViewModel : BaseViewModel() {


    fun onClickInvite() {
        
    }

    fun onClickBack() {
        navDirections.postValue(BackDirections())
    }
}