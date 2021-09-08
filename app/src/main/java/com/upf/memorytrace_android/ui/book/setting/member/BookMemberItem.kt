package com.upf.memorytrace_android.ui.book.setting.member

import com.upf.memorytrace_android.ui.base.BaseItem

internal data class BookMemberItem(
    val uid: Int,
    val nickname: String,
    override val itemId: String = uid.toString()
) : BaseItem