package com.upf.memorytrace_android.ui

import androidx.recyclerview.widget.RecyclerView

abstract class SingleItemAdapter<T: Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var item: T? = null

    override fun getItemCount(): Int {
        return 1
    }

    fun getItem(): T? {
        return item
    }

    fun setItem(item: T?) {
        if (this.item != item) {
            this.item = item
            notifyItemChanged(0)
        }
    }
}