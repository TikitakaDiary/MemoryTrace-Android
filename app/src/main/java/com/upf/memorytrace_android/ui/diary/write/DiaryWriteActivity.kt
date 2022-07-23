package com.upf.memorytrace_android.ui.diary.write

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.color.toColorInt
import com.upf.memorytrace_android.databinding.ActivityDiaryWriteBinding
import com.upf.memorytrace_android.databinding.LayoutDiaryWriteSelectColorContainerBinding
import com.upf.memorytrace_android.extension.*
import com.upf.memorytrace_android.ui.diary.write.color.ColorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryWriteActivity : AppCompatActivity() {

    private val viewModel: DiaryWriteViewModel by viewModels()

    private var selectColorBinding: LayoutDiaryWriteSelectColorContainerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val diaryId: Int? =
            intent.getIntExtra(EXTRA_INPUT_DIARY_ID, -1).takeIf { it != -1 }
        val originalDiary: DiaryWriteContentUiModel? =
            intent.getParcelableExtra(EXTRA_INPUT_ORIGINAL_DIARY)

        repeatOnStart {
            launch {
                viewModel.contentUiModel.collect {
                    binding.writeTitle.setTextIfNew(it.title)
                    binding.writeContents.setTextIfNew(it.content)
                    binding.writeWriter.text = it.writerName
                    binding.writeDate.text = it.date
                    binding.setPolaroidImage(it.image)
                }
            }

            launch {
                viewModel.toolbarState.collect { toolbarState ->
                    binding.writeToolbarTitle.isVisible =
                        toolbarState != DiaryWriteToolbarState.EDIT

                    when (toolbarState) {
                        DiaryWriteToolbarState.WRITE -> {
                            binding.writeToolbarTitle.text = getString(R.string.write_create_diary)
                            binding.setBackButtonArrow()
                            binding.setToolbarButton(R.string.write_delivery) {
                                // Todo
                            }
                        }
                        DiaryWriteToolbarState.EDIT -> {
                            binding.setBackButtonCancel()
                            binding.setToolbarButton(R.string.write_save) {
                                // Todo
                            }
                        }
                        DiaryWriteToolbarState.SELECT_COLOR -> {
                            binding.writeToolbarTitle.text =
                                getString(R.string.write_select_color_title)
                            binding.setToolbarButton(R.string.write_save) {
                                viewModel.onSaveSelectColorClick()
                            }
                            binding.setBackButtonClose {
                                viewModel.dismissSelectColorLayout()
                            }
                        }
                        DiaryWriteToolbarState.ATTACH_STICKER -> {
                            binding.setBackButtonClose { }
                            // Todo
                        }
                    }
                }
            }

            launch {
                viewModel.selectColorUiModel.collect { uiModel ->
                    binding.applySelectColorBinding(isNeedInflate = uiModel.isShowing) {
                        if (uiModel.isShowing) {
                            it.root.isVisible = true
                            val colorAdapter =
                                (it.recyclerviewSelectColor.adapter as? ColorAdapter)
                            colorAdapter?.submitList(uiModel.colorList)
                            clearFocusAndHideSoftInput()
                        } else {
                            it.root.isVisible = false
                        }
                    }
                }
            }
        }

        observeEvent(viewModel.errorEvent) { event ->
            when (event) {
                is DiaryWriteErrorEvent.WrongAccess -> {
                    toast("잘못된 접근입니다.")
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }

        binding.writeSelectImageButton.setOnDebounceClickListener {
            viewModel.onClickSelectColorButton()
        }

        viewModel.loadDiary(diaryId, originalDiary)
    }

    private fun ActivityDiaryWriteBinding.setToolbarButton(
        @StringRes labelResId: Int,
        onClick: () -> Unit
    ) {
        writeToolbarButton.text = getString(labelResId)
        writeToolbarButton.setOnDebounceClickListener {
            onClick.invoke()
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonArrow() {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_back)
            it.setOnDebounceClickListener {
                viewModel.showCancelConfirmDialogIfNeed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonClose(closeAction: () -> Unit) {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_x)
            it.setOnDebounceClickListener {
                viewModel.restorePreviousToolbarState()
                closeAction.invoke()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonCancel() {
        useTextBackButton {
            it.text = getString(R.string.write_exit_cancel)
            it.setOnDebounceClickListener {
                viewModel.showCancelConfirmDialogIfNeed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.useTextBackButton(
        action: (writeToolbarBackButtonText: TextView) -> Unit
    ) {
        with(writeToolbarBackButtonImage) {
            isVisible = false
            setOnDebounceClickListener { }
        }
        with(writeToolbarBackButtonText) {
            isVisible = true
            action.invoke(this)
        }
    }

    private fun ActivityDiaryWriteBinding.useImageBackButton(
        action: (writeToolbarBackButtonImage: ImageView) -> Unit
    ) {
        with(writeToolbarBackButtonText) {
            isVisible = false
            setOnDebounceClickListener { }
        }
        with(writeToolbarBackButtonImage) {
            isVisible = true
            action.invoke(this)
        }
    }

    private fun ActivityDiaryWriteBinding.setPolaroidImage(writeImage: WriteImageType) {
        writeNoContentsImage.isVisible = writeImage is WriteImageType.None
        writeStickerContentsImage.isVisible = writeImage !is WriteImageType.None

        when (writeImage) {
            is WriteImageType.Image -> {
                Glide.with(root.context).load(writeImage.uri).into(writePolaroidImage)
            }
            is WriteImageType.Color -> {
                writePolaroidImage.setImageDrawable(ColorDrawable(writeImage.color.toColorInt()))
            }
            else -> { /* Nothing to do */ }
        }
    }

    private fun ActivityDiaryWriteBinding.applySelectColorBinding(
        isNeedInflate: Boolean,
        apply: (LayoutDiaryWriteSelectColorContainerBinding) -> Unit
    ) {
        if (isNeedInflate && selectColorBinding == null) {
            val view = stubWriteSelectColorContainer.inflate()
            selectColorBinding =
                LayoutDiaryWriteSelectColorContainerBinding.bind(view).apply {
                    recyclerviewSelectColor.itemAnimator = null
                    recyclerviewSelectColor.adapter = ColorAdapter()
                }
        }
        selectColorBinding?.apply(apply)
    }

    class DiaryWriteContract : ActivityResultContract<Input, Output?>() {
        override fun createIntent(context: Context, input: Input): Intent {
            return Intent(context, DiaryWriteActivity::class.java).apply {
                if (input is Input.Edit) {
                    putExtra(EXTRA_INPUT_DIARY_ID, input.diaryId)
                    putExtra(EXTRA_INPUT_ORIGINAL_DIARY, input.originalDiary)
                }
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Output? {
            return if (intent == null || resultCode != RESULT_OK) {
                null
            } else {
                val isNewDiary = intent.getBooleanExtra(EXTRA_OUTPUT_IS_NEW_DIARY, true)
                val diary: DiaryWriteContentUiModel? = intent.getParcelableExtra(EXTRA_OUTPUT_DIARY)

                return if (diary == null) {
                    null
                } else {
                    Output(
                        isNewDiary = isNewDiary,
                        diary = diary
                    )
                }
            }
        }
    }

    sealed class Input {
        data class Edit(
            val diaryId: Int,
            val originalDiary: DiaryWriteContentUiModel,
        ) : Input()

        object New : Input()
    }

    data class Output(
        val isNewDiary: Boolean,
        val diary: DiaryWriteContentUiModel
    )

    companion object {
        private const val EXTRA_INPUT_DIARY_ID = "diaryId"
        private const val EXTRA_INPUT_ORIGINAL_DIARY = "originalDiary"

        private const val EXTRA_OUTPUT_IS_NEW_DIARY = "isNewDiary"
        private const val EXTRA_OUTPUT_DIARY = "diary"
    }
}