package com.upf.memorytrace_android.ui.book.setting.member

import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseListAdapter

internal class BookMemberAdapter : BaseListAdapter<BookMemberItem>() {
    override fun getItemViewTypeByItemType(item: BookMemberItem) = R.layout.item_member
}