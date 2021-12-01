package com.upf.memorytrace_android.ui.diary.detail.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentDetailBinding
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.detail.domain.DiaryDetail
import com.upf.memorytrace_android.util.setAnimationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val detailViewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = detailViewModel

        val slideUpAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_up).apply {
                setAnimationListener(onAnimationStart = { binding.bottomLayout.isVisible = true })
            }

        val slideDownAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_bottom).apply {
                setAnimationListener(onAnimationStart = { binding.bottomLayout.isVisible = false })
            }

        detailViewModel.run {
            fetchDiaryDetail(args.diaryId)

            repeatOnStart {
                launch {
                    isShowBottomLayout.collect {
                        binding.bottomLayout.run {
                            if (it) {
                                startAnimation(slideUpAnimation)
                            } else {
                                startAnimation(slideDownAnimation)
                            }
                        }
                    }
                }

                launch {
                    uiState.collect { state ->
                        if (state is UiState.Success<DiaryDetail>) {
                            binding.diaryDetail = state.data
                        }
                    }
                }
            }


            observeEvent(uiEvent) {
                DetailFragmentDirections.actionDetailFragmentToCommentListFragment(args.diaryId).run {
                    findNavController().navigate(this)
                }
            }
        }

        binding.scrollView.run {
            setOnScrollChangeListener { _, _, _, _, _ ->
                detailViewModel.setBottomLayoutVisibility(
                    canScrollVertically(1).not() || canScrollVertically(-1).not()
                )
            }
        }
    }
}