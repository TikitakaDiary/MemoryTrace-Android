package com.upf.memorytrace_android.ui.diary

import android.os.Bundle
import android.view.View
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentDiaryBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.viewmodel.DiaryViewModel

internal class DiaryFragment : BaseFragment<DiaryViewModel, FragmentDiaryBinding>() {
    override val layoutId = R.layout.fragment_diary
    override val viewModelClass = DiaryViewModel::class

    private val diaryListAdapter = DiaryListOfMonthAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        observe(viewModel.listType) {
            diaryListAdapter.changeListType(
                it,
                viewModel.diaryOfMonthList.value
            )
        }
        observe(viewModel.diaryOfMonthList) { diaryListAdapter.submitList(it) }
    }

    private fun initializeRecyclerView() {
        binding.rv.run {
            adapter = diaryListAdapter.apply { setViewHolderViewModel(this@DiaryFragment.viewModel) }
        }
    }

}