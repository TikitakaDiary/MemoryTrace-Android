package com.upf.memorytrace_android.ui.diary.list.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentDiaryBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.list.presentation.adapter.DiaryLinearAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryListFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val diaryListViewModel: DiaryListViewModel by viewModels()

    private val navArgs by navArgs<DiaryListFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        diaryListViewModel.initializeDiaryList(navArgs.bid)

        binding.viewModel = diaryListViewModel

        val diaryListAdapter = DiaryLinearAdapter()

        with(binding.recyclerviewDiaries) {
            adapter = diaryListAdapter
            setOnScrollChangeListener { v, _, _, _, _ ->
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                if (range == 0 || offset >= range - 10) {
                    diaryListViewModel.loadDiaryList()
                }
            }
        }

//        observe(diaryListViewModel.listType) {
//            diaryListAdapter.changeListType(
//                it,
//                diaryListViewModel.diaryOfMonthList.value
//            )
//            diaryListViewModel.initializeDiaryList()
//        }

        repeatOnStart {
            launch {
                diaryListViewModel.uiState.collect {
                    diaryListAdapter.submitList(it.diaries)
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
}