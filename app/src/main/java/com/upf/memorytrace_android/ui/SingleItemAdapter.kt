package com.upf.memorytrace_android.ui

import androidx.recyclerview.widget.RecyclerView

abstract class SingleItemAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int {
        return 1
    }
}