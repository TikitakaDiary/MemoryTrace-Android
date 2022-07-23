package com.upf.memorytrace_android.ui.diary.write

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.ActivityDiaryWriteBinding
import com.upf.memorytrace_android.extension.*
import com.upf.memorytrace_android.extension.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryWriteActivity : AppCompatActivity() {

    private val viewModel: DiaryWriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val diaryId: Int? =
            intent.getIntExtra(EXTRA_INPUT_DIARY_ID, -1).takeIf { it != -1 }
        val originalDiary: DiaryWriteUiModel? =
            intent.getParcelableExtra(EXTRA_INPUT_ORIGINAL_DIARY)

        repeatOnStart {
            launch {
                viewModel.diaryWriteUiModel.collect {
                    binding.writeTitle.setTextIfNew(it.title)
                    binding.writeContents.setTextIfNew(it.content)
                    binding.writeWriter.text = it.writerName
                    binding.writeDate.text = it.date
                    binding.setPolaroidImage(it.image)
                }
            }

            launch {
                viewModel.diaryDiaryWriteToolbarState.collect { toolbarState ->
                    binding.writeToolbarTitle.isVisible =
                        toolbarState != DiaryWriteToolbarState.EDIT

                    when (toolbarState) {
                        DiaryWriteToolbarState.WRITE -> {
                            binding.writeToolbarTitle.text = getString(R.string.write_create_diary)
                            binding.writeToolbarButton.text = getString(R.string.write_delivery)
                            binding.setBackButtonArrow()
                        }
                        DiaryWriteToolbarState.EDIT -> {
                            binding.writeToolbarButton.text = getString(R.string.write_save)
                            binding.setBackButtonCancel()
                        }
                        DiaryWriteToolbarState.SELECT_COLOR -> {
                            binding.setBackButtonClose()
                            // Todo
                        }
                        DiaryWriteToolbarState.ATTACH_STICKER -> {
                            binding.setBackButtonClose()
                            // Todo
                        }
                    }
                }
            }
        }

        observeEvent(viewModel.diaryWriteErrorEvent) { event ->
            when (event) {
                is DiaryWriteErrorEvent.WrongAccess -> {
                    toast("잘못된 접근입니다.")
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }

        viewModel.loadDiary(diaryId, originalDiary)
    }

    private fun ActivityDiaryWriteBinding.setBackButtonArrow() {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_back)
            it.setOnDebounceClickListener {
                viewModel.showCancelConfirmDialogIfNeed()
            }
        }
    }

    private fun ActivityDiaryWriteBinding.setBackButtonClose() {
        useImageBackButton {
            it.setImageResource(R.drawable.ic_x)
            it.setOnDebounceClickListener {
                viewModel.restorePreviousToolbarState()
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
                writePolaroidImage.setImageDrawable(ColorDrawable(writeImage.color.toInt()))
            }
            else -> { /* Nothing to do */ }
        }
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
                val diary: DiaryWriteUiModel? = intent.getParcelableExtra(EXTRA_OUTPUT_DIARY)

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
            val originalDiary: DiaryWriteUiModel,
        ) : Input()

        object New : Input()
    }

    data class Output(
        val isNewDiary: Boolean,
        val diary: DiaryWriteUiModel
    )

    companion object {
        private const val EXTRA_INPUT_DIARY_ID = "diaryId"
        private const val EXTRA_INPUT_ORIGINAL_DIARY = "originalDiary"

        private const val EXTRA_OUTPUT_IS_NEW_DIARY = "isNewDiary"
        private const val EXTRA_OUTPUT_DIARY = "diary"
    }
}