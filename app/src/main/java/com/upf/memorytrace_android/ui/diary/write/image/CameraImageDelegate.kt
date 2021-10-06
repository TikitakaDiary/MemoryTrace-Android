package com.upf.memorytrace_android.ui.diary.write.image

import android.app.Application
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.upf.memorytrace_android.BuildConfig
import com.upf.memorytrace_android.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraImageDelegate(private val application: Application) {

    companion object {
        private const val DATE_FORMAT = "yyMMdd_HH:mm:ss"
    }

    fun createImageUri() = createImageUri(createImageFile())

    private fun createImageUri(imageFile: File): Uri {
        return FileProvider.getUriForFile(application, BuildConfig.APPLICATION_ID + ".provider", imageFile)
    }

    private fun createImageFile() : File {
        // Android 10 부터는 파일을 MediaStore 에 저장해야합니다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return createTempFile(storageDir)
        }
        // Android 9 이하는 기기 갤러리에 이미지를 직접 생성합니다.
        else {
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                application.getString(R.string.app_name)
            )

            if(!storageDir.exists())
                storageDir.mkdir()

            return createTempFile(storageDir)
        }
    }

    private fun createTempFile(storageDir: File?): File {
        return File.createTempFile(
            SimpleDateFormat(DATE_FORMAT, Locale.KOREAN).format(Date()),
            ".jpeg",
            storageDir
        )
    }
}