package com.upf.memorytrace_android.ui.diary.write

sealed class DiaryWriteErrorEvent {
    object WrongAccess: DiaryWriteErrorEvent()
    data class FailPost(val message: String?): DiaryWriteErrorEvent()
}