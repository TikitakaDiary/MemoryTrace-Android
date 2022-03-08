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
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.BindingAdapters.isVisibleIfTrue
import com.upf.memorytrace_android.ui.SingleItemAdapter
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryGridAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryLinearAdapter
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.MyTurnHeaderViewHolder
import com.upf.memorytrace_android.ui.diary.list.presentation.viewholder.OtherTurnHeaderViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryListFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val diaryListViewModel: DiaryListViewModel by viewModels()

    private val navArgs by navArgs<DiaryListFragmentArgs>()

    private val diaryLinearAdapter: DiaryLinearAdapter by lazy { DiaryLinearAdapter() }
    private val diaryGridAdapter: DiaryGridAdapter by lazy { DiaryGridAdapter() }
    private val myTurnHeaderAdapter: SingleItemAdapter<Unit, MyTurnHeaderViewHolder> by lazy {
        MyTurnHeaderViewHolder.createAdapter { diaryListViewModel.writeDiary() }
    }
    private val otherTurnHeaderAdapter: SingleItemAdapter<PinchInfoUiState, OtherTurnHeaderViewHolder> by lazy {
        OtherTurnHeaderViewHolder.createAdapter()
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
    private var currentHeaderAdapter: SingleItemAdapter<*, *>? = null
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
                diaryListViewModel.diaryListUiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect {
                        binding.progressbar.isVisibleIfTrue(it)
                    }
            }
            launch {
                diaryListViewModel.diaryListUiState
                    .map { it.listContents }
                    .distinctUntilChanged { old, new -> old == new }
                    .collect {
                        it.render()
                    }
            }
            launch {
                diaryListViewModel.pinchInfoUiState
                    .distinctUntilChanged { old, new -> old == new }
                    .collect {
                        otherTurnHeaderAdapter.setItem(it)
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

    private fun DiaryListContentUiState.render() {
        val oldHeaderAdapter = currentHeaderAdapter
        val newHeaderAdapter = if (isMyTurn) {
            myTurnHeaderAdapter
        } else {
            otherTurnHeaderAdapter
        }
        currentHeaderAdapter = newHeaderAdapter

        val oldDiaryAdapter = currentDiaryAdapter
        val newDiaryAdapter =
            if (listType == DiaryListType.LINEAR && oldDiaryAdapter is DiaryGridAdapter) {
                binding.recyclerviewDiaries.layoutManager = diaryLinearLayoutManager
                diaryLinearAdapter
            } else if (listType == DiaryListType.GRID && oldDiaryAdapter is DiaryLinearAdapter) {
                binding.recyclerviewDiaries.layoutManager = diaryGridLayoutManager
                diaryGridAdapter
            } else {
                oldDiaryAdapter
            }
        newDiaryAdapter.submitList(diaries)
        currentDiaryAdapter = newDiaryAdapter

        // onViewCreated() 가 호출되면서 초기화(force)되는 데이터는 항상 어댑터 재생성
        if (isForce ||
            oldDiaryAdapter != newDiaryAdapter ||
            oldHeaderAdapter != newHeaderAdapter
        ) {
            binding.recyclerviewDiaries.adapter =
                ConcatAdapter(newHeaderAdapter, newDiaryAdapter)
        }
    }

    companion object {
        private const val GRID_SPAN_COUNT = 4
    }
}