package com.upf.memorytrace_android.ui.diary.write

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upf.memorytrace_android.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun startWriteActivity(context: Context) {
            context.startActivity(Intent(context, WriteActivity::class.java))
        }
    }
}