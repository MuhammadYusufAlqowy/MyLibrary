package com.yadev.mycustomlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadev.mycustomlibrary.databinding.FragmentTes2Binding
import com.yadev.mycustomlibrary.databinding.FragmentTesBinding
import com.yadev.mylibrary.adapter.ViewPager2FragmentAdapter
import com.yadev.mylibrary.fragment.BaseFragment

class Tes2Fragment : BaseFragment<FragmentTes2Binding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTes2Binding {
        return FragmentTes2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout.apply {
            val adapter = ViewPager2FragmentAdapter(requireActivity())
            adapter.setUp(mutableListOf(TesFragment(),TesFragment()))
            viewPager.adapter = adapter
        }
    }

}