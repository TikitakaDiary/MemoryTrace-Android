package com.upf.memorytrace_android.sticker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.upf.memorytrace_android.databinding.FragmentSelectStickerBinding
import com.upf.memorytrace_android.sticker.adapter.StickerPackageRecyclerViewAdapter
import com.upf.memorytrace_android.sticker.model.StickerItemUiModel
import com.upf.memorytrace_android.sticker.model.StickerPackageUiModel

class SelectStickerDialogFragment : BottomSheetDialogFragment() {

    private var onStickerClick: (Int) -> Unit = {}

    private val packages: List<StickerPackageUiModel> = StickerPackage.packages.map { packagePair ->
        StickerPackageUiModel(
            imageResource = packagePair.first,
            stickers = packagePair.second.map { stickerImageResource ->
                StickerItemUiModel(
                    imageResource = stickerImageResource,
                    onClick = {
                        onStickerClick.invoke(stickerImageResource)
                    }
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSelectStickerBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectStickerBinding.bind(view)

        binding.selectStickerViewpager.adapter = StickerPackageRecyclerViewAdapter().apply {
            submitList(packages)
        }

        TabLayoutMediator(binding.selectStickerTabLayout, binding.selectStickerViewpager) { tab, pos ->
            StickerPackage.packages.getOrNull(pos)?.let {
                tab.setIcon(it.first)
            }
        }.attach()
    }

    fun setOnStickClick(listener: (Int) -> Unit) {
        this.onStickerClick = listener
    }
}