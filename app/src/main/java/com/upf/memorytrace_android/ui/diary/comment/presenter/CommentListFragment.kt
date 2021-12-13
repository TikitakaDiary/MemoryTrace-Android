package com.upf.memorytrace_android.ui.diary.comment.presenter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentCommentListBinding
import com.upf.memorytrace_android.extension.hideKeyboard
import com.upf.memorytrace_android.extension.repeatOnStart
import com.upf.memorytrace_android.extension.showKeyboard
import com.upf.memorytrace_android.ui.UiState
import com.upf.memorytrace_android.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CommentListFragment :
    BindingFragment<FragmentCommentListBinding>(R.layout.fragment_comment_list) {

    private val commentListViewModel: CommentListViewModel by viewModels()

    private val args: CommentListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel = commentListViewModel
            recyclerViewComments.adapter = CommentListAdapter(
                onReplyClick = commentListViewModel::startReplyingMode,
                onItemLongClick = {
                    showDialog(
                        requireContext(),
                        getString(R.string.comment_delete_dialog_title),
                        getString(R.string.comment_delete_dialog_content),
                        getString(R.string.comment_delete_dialog_positive),
                        positive = {
                            commentListViewModel.deleteComment(it)
                        }
                    )
                }
            )
        }

        repeatOnStart {
            commentListViewModel.uiState.collect {
                if (it is UiState.Loading) {
                    hideKeyboard()
                }
            }
        }

        commentListViewModel.run {
            fetchComments(args.diaryId)

            currentReplying.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.textInputComment.requestFocus()
                    showKeyboard()
                }
            }
        }
    }
}