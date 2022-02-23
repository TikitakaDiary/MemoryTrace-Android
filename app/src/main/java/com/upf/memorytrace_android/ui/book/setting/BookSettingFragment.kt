package com.upf.memorytrace_android.ui.book.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.databinding.FragmentBookSettingBinding
import com.upf.memorytrace_android.extension.copyToClipboard
import com.upf.memorytrace_android.extension.observeEvent
import com.upf.memorytrace_android.extension.toast
import com.upf.memorytrace_android.ui.base.BindingFragment
import com.upf.memorytrace_android.util.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSettingFragment :
    BindingFragment<FragmentBookSettingBinding>(R.layout.fragment_book_setting) {
    private val viewModel: BookSettingViewModel by viewModels()

    private val navArgs by navArgs<BookSettingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setBookId(navArgs.bid)
        binding.viewModel = viewModel

        observeEvent(viewModel.uiEvent) { event ->
            when (event) {
                is BookSettingViewModel.Event.Leave -> {
                    showDialog(
                        requireActivity(),
                        R.string.leave_book_section_title,
                        R.string.leave_book_message,
                        R.string.write_exit_exit
                    ) { viewModel.leaveBook() }
                }
                is BookSettingViewModel.Event.LeaveDone -> {
                    BookSettingFragmentDirections
                        .actionBookSettingFragmentToBookListFragment().run {
                            findNavController().navigate(this)
                        }
                }
                is BookSettingViewModel.Event.EditBook -> {
                    BookSettingFragmentDirections
                        .actionBookSettingFragmentToCreateBookFragment(event.bookId).run {
                            findNavController().navigate(this)
                        }
                }
                is BookSettingViewModel.Event.ShowMember -> {
                    BookSettingFragmentDirections
                        .actionBookSettingFragmentToMemberSettingFragment(event.bookId).run {
                            findNavController().navigate(this)
                        }
                }
                is BookSettingViewModel.Event.Error -> {
                    toast(event.message)
                }
                is BookSettingViewModel.Event.WrongAccess -> {
                    toast(getString(R.string.wrong_access))
                    BookSettingFragmentDirections
                        .actionBookSettingFragmentToBookListFragment().run {
                            findNavController().navigate(this)
                        }
                }
                is BookSettingViewModel.Event.CopyInvitationCode -> {
                    context?.copyToClipboard("invite_code", event.invitationCode)
                    toast(getString(R.string.toast_copy_invitation_code))
                }
            }
        }
    }
}