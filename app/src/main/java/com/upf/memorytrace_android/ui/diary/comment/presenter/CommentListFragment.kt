package com.upf.memorytrace_android.ui.diary.comment.presenter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentCommentListBinding
import com.upf.memorytrace_android.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentListFragment :
    BindingFragment<FragmentCommentListBinding>(R.layout.fragment_comment_list) {

    private val commentListViewModel: CommentListViewModel by viewModels()

    private val args: CommentListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = commentListViewModel
            recyclerViewComments.adapter = CommentListAdapter()
        }

        commentListViewModel.fetchComments(args.diaryId)
    }
}