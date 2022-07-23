package com.upf.memorytrace_android.ui.diary.write

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Parcelable
import com.upf.memorytrace_android.color.UserColor
import com.upf.memorytrace_android.ui.diary.write.color.ColorItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryWriteContentUiModel(
    val title: String = "",
    val date: String = "",
    val writerName: String = "",
    val content: String = "",
    val image: WriteImageType = WriteImageType.None,
    val stickerEdited: Boolean = false,
) : Parcelable {
    val canPost: Boolean
        get() = title.isNotEmpty()
                && content.isNotEmpty()
                && image !is WriteImageType.None
}

sealed class WriteImageType : Parcelable {
    @Parcelize
    data class Image(
        val uri: Uri = Uri.EMPTY
    ) : WriteImageType()

    @Parcelize
    data class Color(
        val color: UserColor = UserColor.getDefaultColor()
    ) : WriteImageType()

    @Parcelize
    object None : WriteImageType()
}

enum class SelectImageType {
    CAMERA, ALBUM, COLOR
}

enum class DiaryWriteToolbarState {
    EDIT, WRITE
}

data class DiaryWriteSelectColorUiModel(
    val colorList: List<ColorItemUiModel> = emptyList(),
    val isShowing: Boolean = false,
    val previousImageType: WriteImageType = WriteImageType.None
)

sealed interface DiaryWriteEvent {
    data class ShowEditConfirmDialog(val nextImageType: SelectImageType) : DiaryWriteEvent
    object StartGalleryActivity : DiaryWriteEvent
    object StartCameraActivity : DiaryWriteEvent
    object FinishWriteActivity : DiaryWriteEvent
    object ShowFinishConfirmDialog : DiaryWriteEvent
    object PostDone : DiaryWriteEvent
    object EditDone : DiaryWriteEvent
    data class AddSticker(val stickerDrawable: Drawable): DiaryWriteEvent
}

sealed interface DiaryWriteErrorEvent {
    object WrongAccess: DiaryWriteErrorEvent
    data class FailPost(val message: String?): DiaryWriteErrorEvent
}