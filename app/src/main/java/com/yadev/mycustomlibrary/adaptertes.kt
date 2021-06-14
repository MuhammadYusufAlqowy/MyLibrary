package com.yadev.mycustomlibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadev.mycustomlibrary.databinding.LayoutTesBinding
import com.yadev.mylibrary.MyBitmapTransformation
import com.yadev.mylibrary.adapter.BaseMyImageSliderAdapter
import com.yadev.mylibrary.setImageUrl

class adaptertes : BaseMyImageSliderAdapter<String, LayoutTesBinding>(
    mutableListOf(
        "https://paketnasi.co.id/uploads/user/slider/19.png",
        "https://paketnasi.co.id/uploads/user/slider/20.png",
        "https://paketnasi.co.id/uploads/user/slider/19.png",
        "https://paketnasi.co.id/uploads/user/slider/21.png"
    )
) {
    override fun setViewBinding(parent: ViewGroup): LayoutTesBinding {
        return LayoutTesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(item: String, view: LayoutTesBinding) {
        view.apply {
            img.setImageUrl(item, type = MyBitmapTransformation.CENTER_INSIDE)
        }
    }
}