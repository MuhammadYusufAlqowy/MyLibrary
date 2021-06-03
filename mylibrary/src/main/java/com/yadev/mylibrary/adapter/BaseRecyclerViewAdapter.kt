package com.yadev.mylibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(var listItem: MutableList<T>, @LayoutRes val res: Int) :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(res, parent, false))
    }

    override fun getItemCount() = listItem.size


}