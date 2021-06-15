package com.upf.memorytrace_android.ui.book

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.api.model.Book
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentCreateBookBinding
import com.upf.memorytrace_android.databinding.ItemColorListBinding
import com.upf.memorytrace_android.extension.observe
import com.upf.memorytrace_android.util.Colors
import com.upf.memorytrace_android.util.hideKeyboard
import com.upf.memorytrace_android.util.visibleOrGone
import com.upf.memorytrace_android.viewmodel.CreateBookViewModel

internal class CreateBookFragment : BaseFragment<CreateBookViewModel, FragmentCreateBookBinding>() {
    override val layoutId = R.layout.fragment_create_book
    override val viewModelClass = CreateBookViewModel::class
    private val adapter: ColorListAdapter by lazy {
        ColorListAdapter(Colors.getColors())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //todo; arg를 받았을 경우 해당 값으로 수정할 수 있도록 그려줘야한다.
//        receiveArgFromOtherView<Book>("book") {
//            binding.title.setText(it.title)
//            viewModel.setSelectedColor(Colors.getColor(it.bgColor))
//            binding.toolbar.title = getString(R.string.setting_cover_edit)
//        }

        setupRecyclerView()
        initViewModel()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            binding.title.hideKeyboard()
        }
        binding.btnCreate.setOnClickListener { viewModel.createBook(binding.title.text.toString()) }
    }

    private fun initViewModel() {
        observe(viewModel.selectedColor) {
            binding.itemBook.setCardBackgroundColor(it.toInt())
        }
        observe(viewModel.createBookSuccess) {
            //TODO: 리스트에 데이터 업데이트 되도록 확인 및 처리 필요
            findNavController().popBackStack()
            binding.title.hideKeyboard()
        }
    }

    private fun setupRecyclerView() {
        binding.list.setHasFixedSize(true)
        binding.list.adapter = adapter
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
                    notifyDataSetChanged()
                }
            }
        }
    }


}