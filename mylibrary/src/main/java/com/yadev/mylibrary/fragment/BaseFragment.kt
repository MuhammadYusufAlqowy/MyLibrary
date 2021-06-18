package com.yadev.mylibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V:ViewBinding> : Fragment() {
    lateinit var layout: V
    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val VERTICAL = RecyclerView.VERTICAL
    val HORIZONTAL = RecyclerView.HORIZONTAL
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = getViewBinding(inflater, container)
        return layout.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): V
}