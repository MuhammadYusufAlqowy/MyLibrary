package com.yadev.mycustomlibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadev.mycustomlibrary.databinding.FragmentTes2Binding
import com.yadev.mylibrary.adapter.ViewPager2FragmentAdapter
import com.yadev.mylibrary.fragment.BaseFragment

class Tes2Fragment : BaseFragment<FragmentTes2Binding>() {
    override fun setup() {
        layout.apply {
            val adapter = ViewPager2FragmentAdapter(requireActivity())
            adapter.setUp(mutableListOf(TesFragment(), TesFragment()))
            viewPager.adapter = adapter
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTes2Binding {
        return FragmentTes2Binding.inflate(inflater, container, false)
    }
}