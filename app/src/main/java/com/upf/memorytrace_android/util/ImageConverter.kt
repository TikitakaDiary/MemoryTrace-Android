package com.upf.memorytrace_android.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

internal object ImageConverter {
    fun convertViewToBitmap(v: View): Bitmap {
        val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bmp)
        v.draw(canvas)
        return bmp
    }

    fun fileToMultipartBody(file: File, multipartName: String?): MultipartBody.Part? {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(multipartName ?: "img", file.name, requestBody)
    }

    fun convertBitmapToFile(
        bitmap: Bitmap,
        parentDir: File,
        fileName: String?
    ): File? {
        val file = File(parentDir, fileName ?: "duck-z_${System.currentTimeMillis()}.png")
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val byteArray = bos.toByteArray()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            fos.write(byteArray)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun convertBitmapToMultipartBody(
        bitmap: Bitmap,
        parentDir: File,
        fileName: String?,
        multipartName: String?
    ): MultipartBody.Part? {
        return convertBitmapToFile(bitmap, parentDir, fileName)?.let {
            fileToMultipartBody(it, multipartName)
        }
    }
}