package com.upf.memorytrace_android.ui.diary.list.presentation

import android.os.Bundle
import android.view.View
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
import com.upf.memorytrace_android.ui.SingleItemAdapter
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryGridAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryLinearAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.MyTurnHeaderViewHolder
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.OtherTurnHeaderViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryListFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val diaryListViewModel: DiaryListViewModel by viewModels()

    private val navArgs by navArgs<DiaryListFragmentArgs>()

    private val diaryLinearAdapter: DiaryLinearAdapter by lazy { DiaryLinearAdapter() }
    private val diaryGridAdapter: DiaryGridAdapter by lazy { DiaryGridAdapter() }
    private val myTurnHeaderAdapter: SingleItemAdapter<MyTurnHeaderViewHolder> by lazy {
        MyTurnHeaderViewHolder.createAdapter { diaryListViewModel.writeDiary() }
    }
    private val otherTurnHeaderAdapter: SingleItemAdapter<OtherTurnHeaderViewHolder> by lazy {
        OtherTurnHeaderViewHolder.createAdapter { /**Todo: 재촉하기**/ }
    }


    private val diaryLinearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val diaryGridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, GRID_SPAN_COUNT).apply {
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

    // Header 는 기본 값이 없음
    private var currentHeaderAdapter: SingleItemAdapter<*>? = null
    private var currentDiaryAdapter: DiaryAdapter<*> = diaryLinearAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        diaryListViewModel.initializeDiaryList(navArgs.bid)

        binding.viewModel = diaryListViewModel

        with(binding.recyclerviewDiaries) {
            setOnScrollChangeListener { _, _, _, _, _ ->
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
                    if (it.isLoading || it.errorMessage.isNotEmpty()) return@collect

                    val oldHeaderAdapter = currentHeaderAdapter
                    val newHeaderAdapter = if (it.isMyTurn) {
                        myTurnHeaderAdapter
                    } else {
                        otherTurnHeaderAdapter
                    }
                    currentHeaderAdapter = newHeaderAdapter

                    val oldDiaryAdapter = currentDiaryAdapter
                    val newDiaryAdapter =
                        if (it.listType == DiaryListType.LINEAR && oldDiaryAdapter is DiaryGridAdapter) {
                            binding.recyclerviewDiaries.layoutManager = diaryLinearLayoutManager
                            diaryLinearAdapter
                        } else if (it.listType == DiaryListType.GRID && oldDiaryAdapter is DiaryLinearAdapter) {
                            binding.recyclerviewDiaries.layoutManager = diaryGridLayoutManager
                            diaryGridAdapter
                        } else {
                            oldDiaryAdapter
                        }
                    newDiaryAdapter.submitList(it.diaries)
                    currentDiaryAdapter = newDiaryAdapter

                    if (oldDiaryAdapter != newDiaryAdapter || oldHeaderAdapter != newHeaderAdapter) {
                        binding.recyclerviewDiaries.adapter =
                            ConcatAdapter(newHeaderAdapter, newDiaryAdapter)
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