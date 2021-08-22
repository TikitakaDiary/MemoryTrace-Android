package com.upf.memorytrace_android.ui.book.setting

import com.upf.memorytrace_android.base.BaseItem

internal data class BookMemberItem(
    val uid: Int,
    val nickname: String,
    override val itemId: String = uid.toString()
) : BaseItem