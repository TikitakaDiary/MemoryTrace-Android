package com.upf.memorytrace_android.ui.diary.list.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentDiaryBinding
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.BindingAdapters.isVisibleIfTrue
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryGridAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryHeaderAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryLinearAdapter
import com.upf.memorytrace_android.ui.diary.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryListFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val diaryListViewModel: DiaryListViewModel by viewModels()

    private val navArgs by navArgs<DiaryListFragmentArgs>()

    private val diaryLinearAdapter = DiaryLinearAdapter()
    private val diaryGridAdapter = DiaryGridAdapter()
    private val diaryHeaderAdapter = DiaryHeaderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forceInitialize = findNavController()
            .currentBackStackEntry?.savedStateHandle?.get<Boolean>(KEY_FORCE_INITIALIZE) ?: false
        // consume
        findNavController().currentBackStackEntry?.savedStateHandle?.set(KEY_FORCE_INITIALIZE, false)

        diaryListViewModel.initializeDiaryList(navArgs.bid, forceInitialize)

        binding.viewModel = diaryListViewModel

        with(binding.recyclerviewDiariesLinear) {
            setOnScrollChangeListener { _, _, _, _, _ ->
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                if (range == 0 || offset >= range - 10) {
                    diaryListViewModel.loadDiaryList()
                }
            }
            layoutManager = LinearLayoutManager(context)
        }

        with(binding.recyclerviewDiariesGrid) {
            setOnScrollChangeListener { _, _, _, _, _ ->
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                if (range == 0 || offset >= range - 10) {
                    diaryListViewModel.loadDiaryList()
                }
            }
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        // Header
                        if (position == 0) {
                            return GRID_SPAN_COUNT
                        }
                        // Header 를 제외한 itemViewType 산정
                        val viewType = diaryGridAdapter.getItemViewType(position - 1)
                        return if (viewType == DiaryAdapter.VIEW_TYPE_DATE) {
                            GRID_SPAN_COUNT
                        } else {
                            1
                        }
                    }
                }
            }
        }

        binding.recyclerviewDiariesLinear.adapter =
            ConcatAdapter(diaryHeaderAdapter, diaryLinearAdapter)
        binding.recyclerviewDiariesGrid.adapter =
            ConcatAdapter(diaryHeaderAdapter, diaryGridAdapter)

        repeatOnStart {
            launch {
                diaryListViewModel.diaryListUiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect {
                        binding.progressbar.isVisibleIfTrue(it)
                    }
            }
            launch {
                diaryListViewModel.diaryListTypeUiModel
                    .collect {
                        it.applyListType()
                    }
            }
            launch {
                diaryListViewModel.diaryListContentUiModel
                    .collect {
                        it.render()
                    }
            }
            launch {
                diaryListViewModel.diaryHeaderUiModel
                    .collect {
                        diaryHeaderAdapter.setItem(it)
                    }
            }

            diaryListViewModel.run {
                observeEvent(uiEvent) { event ->
                    when (event) {
                        is DiaryListViewModel.Event.Setting -> {
                            DiaryListFragmentDirections
                                .actionDiaryFragmentToBookSettingFragment(event.bookId)
                                .run {
                                    findNavController().navigate(this)
                                }

                        }
                        is DiaryListViewModel.Event.DiaryDetail -> {
                            DiaryListFragmentDirections
                                .actionDiaryFragmentToDetailFragment(event.diaryId)
                                .run {
                                    findNavController().navigate(this)
                                }
                        }
                        is DiaryListViewModel.Event.WriteDiary -> {
                            WriteActivity.startWriteActivity(requireContext())
                        }
                        is DiaryListViewModel.Event.SuccessPinch -> {
                            toast(getString(R.string.pinch_success_toast, event.turnUserName))
                        }
                        is DiaryListViewModel.Event.Error -> {
                            toast(event.errorMessage.takeIf { message ->
                                message.isNotEmpty()
                            } ?: getString(R.string.unknown_error))
                        }
                    }
                }
            }
        }
    }

    private fun DiaryListTypeUiModel.applyListType() {
        binding.recyclerviewDiariesLinear.isVisible = listType == DiaryListType.LINEAR
        binding.recyclerviewDiariesGrid.isVisible = listType == DiaryListType.GRID
    }

    private fun DiaryListContentUiModel.render() {
        diaryLinearAdapter.submitList(diaries)
        diaryGridAdapter.submitList(diaries)
    }

    companion object {
        private const val GRID_SPAN_COUNT = 4
        const val KEY_FORCE_INITIALIZE = "forceInitialize"
    }
}