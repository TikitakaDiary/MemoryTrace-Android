package com.upf.memorytrace_android.extension

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.upf.memorytrace_android.BuildConfig
import java.io.File

fun File.toContentUri(context: Context): Uri {
    val authority = BuildConfig.APPLICATION_ID + ".provider"
    return FileProvider.getUriForFile(context, authority, this)
}