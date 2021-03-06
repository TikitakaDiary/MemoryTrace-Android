package com.upf.memorytrace_android.ui.book.create

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentCreateBookBinding
import com.upf.memorytrace_android.databinding.ItemColorListBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.ui.book.create.sticker.BookStickerBottomSheetFragment
import com.upf.memorytrace_android.util.*
import com.upf.memorytrace_android.util.ImageConverter
import com.xiaopo.flying.sticker.DrawableSticker

internal class CreateBookFragment : BaseFragment<CreateBookViewModel, FragmentCreateBookBinding>() {
    override val layoutId = R.layout.fragment_create_book
    override val viewModelClass = CreateBookViewModel::class
    override val navArgs by navArgs<CreateBookFragmentArgs>()

    private val adapter: ColorListAdapter by lazy {
        ColorListAdapter(Colors.getColors())
    }

    private val stickerDialog by lazy(LazyThreadSafetyMode.NONE) {
        BookStickerBottomSheetFragment(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProperties()
        setBackPressedDispatcher()

        setupRecyclerView()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        with(viewModel.stickerList) {
            clear()
            addAll(binding.stickerView.stickers)
        }
    }

    private fun setProperties() {
        //수정일 경우, title, color, bid, sticker로 update
        binding.stickerView.stickers = this@CreateBookFragment.viewModel.stickerList
    }

    private fun setBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    private fun loadStickerDialog() {
        stickerDialog.show(parentFragmentManager, STICKER_DIALOG_TAG)
    }

    private fun hideStickerDialog() {
        stickerDialog.dismiss()
    }

    private fun attachSticker(@DrawableRes stickerId: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), stickerId)
        binding.stickerView.addSticker(DrawableSticker(drawable))
    }

    private fun initViewModel() {
        observe(viewModel.selectedColor) {
            Colors.fillColorTint(binding.itemBook, it)
            adapter.notifyDataSetChanged()
        }

        observe(viewModel.showStickerResetDialog) { if(it == true) showStickerResetDialog() }
        observe(viewModel.isShowStickerDialog) { loadStickerDialog() }
        observe(viewModel.isHideStickerDialog) { hideStickerDialog() }
        observe(viewModel.addSticker) { attachSticker(it) }
        observe(viewModel.isSaveBook) { saveBook() }
    }

    private fun setupRecyclerView() {
        binding.list.setHasFixedSize(true)
        binding.list.adapter = adapter
    }

    private fun saveBook() {
        binding.stickerView.removeStickerHandler()
        val bitmap: Bitmap? =
            if (binding.stickerView.width != 0 && binding.stickerView.height != 0) ImageConverter.convertViewToBitmap(
                binding.stickerView
            ) else null
        val cacheDir = requireContext().cacheDir
        viewModel.createBook(cacheDir, bitmap)
    }

    private inner class ColorListAdapter(private val colorList: List<Colors>) :
        RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
        override fun getItemCount(): Int = colorList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemColorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(colorList[position])
        }

        private inner class ViewHolder(val binding: ItemColorListBinding) :
            RecyclerView.ViewHolder(binding.root) {


            fun bind(color: Colors) {
                binding.selected.visibleOrGone(viewModel.isSelected(color))
                Colors.fillCircle(binding.circleColor, color)
                itemView.setOnClickListener {
                    viewModel.setSelectedColor(color)
                }
            }
        }
    }

    private fun showExitDialog() {
        showDialog(
            requireActivity(),
            R.string.write_exit_title,
            R.string.create_book_exit_content,
            R.string.write_exit_exit
        ) {
            viewModel.onClickBack()
        }
    }

    private fun showStickerResetDialog() {
        showDialog(
            requireActivity(),
            R.string.sticker_reset_title,
            R.string.sticker_reset_message,
            R.string.edit
        ) {
            viewModel.showStickerDialog(true)
        }
    }

    companion object {
        private const val STICKER_DIALOG_TAG = "StickerDialog"
    }
}