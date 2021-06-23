package com.yadev.mylibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T, V : ViewBinding>(var listItem: MutableList<T>) :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var filterList = listItem
    private var onItemSelectedListener: ((T, Int) -> Unit)? = null
    private var recyclerView: RecyclerView? = null
    lateinit var layout: V
    abstract fun setViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup): V
    abstract fun bind(item: T, view: V, position: Int)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layout = setViewBinding(LayoutInflater.from(parent.context), parent)
        return ViewHolder(layout.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(listItem[position], layout, holder.adapterPosition)
        onItemSelectedListener?.let {
            holder.itemView.setOnClickListener { v ->
                it(listItem[holder.adapterPosition], holder.adapterPosition)
            }
        }
    }

    override fun getItemCount() = listItem.size

    fun searchItem(predicate: (T) -> Boolean) {
        val search = filterList.filter(predicate)
        listItem = search as MutableList<T>
        notifyDataSetChanged()
    }

    fun onItemSelectedListener(listener: ((T, Int) -> Unit)? = null) {
        onItemSelectedListener = listener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}