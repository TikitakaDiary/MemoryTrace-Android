package com.upf.memorytrace_android.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.findNavController
import com.upf.memorytrace_android.R

class BackButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    init {
        setImageResource(R.drawable.ic_arrow_back)
        setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
