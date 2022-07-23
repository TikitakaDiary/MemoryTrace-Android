package com.upf.memorytrace_android.extension

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

@Throws(FileNotFoundException::class)
fun Bitmap.toUri(context: Context, fileName: String, childDirectoryName: String): Uri {
    return toFile(context, fileName, childDirectoryName).toUri()
}

@Throws(FileNotFoundException::class)
fun Bitmap.toFile(context: Context, fileName: String, childDirectoryName: String): File {
    val directory = File(context.cacheDir, childDirectoryName).apply { mkdir() }
    val file = File(directory, fileName)
    val out = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG, 100, out)
    return file
}