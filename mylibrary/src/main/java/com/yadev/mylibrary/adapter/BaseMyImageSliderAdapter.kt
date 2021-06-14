package com.yadev.mylibrary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseMyImageSliderAdapter<T, V : ViewBinding>(val list: MutableList<T>) :
    RecyclerView.Adapter<BaseMyImageSliderAdapter.ViewHolder>() {
    var pageMargin: Int = 0
    var pageOffset: Int = 0
    var fakeList = mutableListOf(list.last()) + list + mutableListOf(list.last())
    var maxHeight = 0

    lateinit var layout: V

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    abstract fun setViewBinding(parent: ViewGroup): V
    abstract fun bind(item: T, view: V)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layout = setViewBinding(parent)
        return ViewHolder(layout.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        layout.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = pageMargin + pageOffset
            rightMargin = pageMargin + pageOffset
        }

        bind(item, layout)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}