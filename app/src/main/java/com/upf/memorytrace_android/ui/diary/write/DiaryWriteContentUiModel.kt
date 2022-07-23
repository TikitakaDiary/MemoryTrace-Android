package com.upf.memorytrace_android.ui.diary.write

import android.net.Uri
import android.os.Parcelable
import com.upf.memorytrace_android.color.UserColor
import com.upf.memorytrace_android.ui.diary.write.color.ColorItemUiModel
import com.upf.memorytrace_android.util.Colors
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryWriteContentUiModel(
    val title: String = "",
    val date: String = "",
    val writerName: String = "",
    val content: String = "",
    val image: WriteImageType = WriteImageType.None,
) : Parcelable

sealed class WriteImageType: Parcelable {
    @Parcelize
    data class Image(
        val uri: Uri = Uri.EMPTY
    ) : WriteImageType()

    @Parcelize
    data class Color(
        val color: UserColor = UserColor.NONE
    ): WriteImageType()

    @Parcelize
    object None : WriteImageType()
}

enum class DiaryWriteToolbarState {
    EDIT, WRITE, SELECT_COLOR, ATTACH_STICKER
}

data class DiaryWriteSelectColorUiModel(
    val colorList: List<ColorItemUiModel> = emptyList(),
    val isShowing: Boolean = false,
    val previousImageType: WriteImageType = WriteImageType.None
)