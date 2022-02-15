package com.upf.memorytrace_android.ui.diary.list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentDiaryBinding
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.list.DiaryListType
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryGridAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryLinearAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryListFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val diaryListViewModel: DiaryListViewModel by viewModels()

    private val navArgs by navArgs<DiaryListFragmentArgs>()

    private val diaryLinearAdapter: DiaryLinearAdapter by lazy { DiaryLinearAdapter() }
    private val diaryGridAdapter: DiaryGridAdapter by lazy { DiaryGridAdapter() }
    private val diaryLinearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val diaryGridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = diaryGridAdapter.getItemViewType(position)
                    return if (viewType == DiaryAdapter.VIEW_TYPE_DATE) {
                        GRID_SPAN_COUNT
                    } else {
                        1
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        diaryListViewModel.initializeDiaryList(navArgs.bid)

        binding.viewModel = diaryListViewModel

        with(binding.recyclerviewDiaries) {
            adapter = diaryLinearAdapter
            setOnScrollChangeListener { v, _, _, _, _ ->
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                if (range == 0 || offset >= range - 10) {
                    diaryListViewModel.loadDiaryList()
                }
            }
        }

        repeatOnStart {
            launch {
                diaryListViewModel.uiState.collect {
                    val oldAdapter = binding.recyclerviewDiaries.adapter as DiaryAdapter<*>

                    if (it.listType == DiaryListType.LINEAR && oldAdapter is DiaryGridAdapter) {
                        binding.recyclerviewDiaries.adapter = diaryLinearAdapter.apply {
                            submitList(it.diaries)
                        }
                        binding.recyclerviewDiaries.layoutManager = diaryLinearLayoutManager
                    } else if (it.listType == DiaryListType.GRID && oldAdapter is DiaryLinearAdapter) {
                        binding.recyclerviewDiaries.adapter = diaryGridAdapter.apply {
                            submitList(it.diaries)
                        }
                        binding.recyclerviewDiaries.layoutManager = diaryGridLayoutManager
                    } else {
                        oldAdapter.submitList(it.diaries)
                    }
                }
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
                        DiaryListFragmentDirections
                            .actionDiaryFragmentToWriteFragment(event.bookId, null)
                            .run {
                                findNavController().navigate(this)
                            }
                    }
                }
            }
        }
    }

    companion object {
        private const val GRID_SPAN_COUNT = 4
    }
}