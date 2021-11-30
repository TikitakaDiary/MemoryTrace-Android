package com.upf.memorytrace_android.ui.diary.comment.presenter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentCommentListBinding
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.ui.diary.comment.domain.Comment
import com.upf.memorytrace_android.util.InputModeLifecycleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CommentListFragment :
    BindingFragment<FragmentCommentListBinding>(R.layout.fragment_comment_list) {

    private val commentListViewModel: CommentListViewModel by viewModels()

    private val args: CommentListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(
            InputModeLifecycleHelper(
                activity?.window,
                InputModeLifecycleHelper.Mode.ADJUST_RESIZE
            )
        )

        commentListViewModel.fetchComments(args.diaryId)

        repeatOnStart {
            commentListViewModel.uiState.collect {
                if (it is UiState.Success<*>) {
                    Log.d("TESTT", it.toString())
                }
            }
        }
    }
}