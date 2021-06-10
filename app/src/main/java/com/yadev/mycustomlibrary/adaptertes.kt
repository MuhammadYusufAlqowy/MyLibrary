package com.yadev.mycustomlibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadev.mycustomlibrary.databinding.LayoutTesBinding
import com.yadev.mylibrary.adapter.BaseMyImageSliderAdapter

class adaptertes : BaseMyImageSliderAdapter<Int, LayoutTesBinding>(mutableListOf(R.drawable.ic_gallery,R.drawable.ic_gallery, R.drawable.ic_gallery)) {

    override fun setViewBinding(parent: ViewGroup): LayoutTesBinding {
        return LayoutTesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(item: Int, view: LayoutTesBinding) {
        view.apply {
            img.setImageResource(item)
        }
    }
}