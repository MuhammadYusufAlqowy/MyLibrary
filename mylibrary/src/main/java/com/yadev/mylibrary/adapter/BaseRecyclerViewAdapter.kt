package com.yadev.mylibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T, V : ViewBinding>(var listItem: MutableList<T>) :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var saveList = listItem
    private var OnItemSelectedListener: ((T, Int) -> Unit)? = null
    var liveData: MutableLiveData<MutableList<T>> = MutableLiveData()
    var emptyView: View? = null
    private var recyclerView: RecyclerView? = null
    lateinit var layout: V
    abstract fun setViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup): V
    abstract fun bind(item: T, view: V)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layout = setViewBinding(LayoutInflater.from(parent.context), parent)
        return ViewHolder(layout.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(listItem[position], layout)
        liveData.postValue(listItem)
        holder.itemView.setOnClickListener {
            OnItemSelectedListener?.let {
                it(
                    listItem[holder.adapterPosition],
                    holder.adapterPosition
                )
            }
        }

    }


    override fun getItemCount(): Int {
        /*if (emptyView != null) {
            if (listItem.isNullOrEmpty()) {
                emptyView?.visibility = VISIBLE
                recyclerView?.visibility = GONE
            } else {
                emptyView?.visibility = GONE
                recyclerView?.visibility = VISIBLE
            }
        }*/
        return listItem.size

    }

    fun searchItem(predicate: (T) -> Boolean): MutableList<T> {
        val search = saveList.filter(predicate)
        listItem = search as MutableList<T>
        notifyDataSetChanged()
        return listItem
    }

    fun OnItemSelectedListener(listener: ((T, Int) -> Unit)) {
        OnItemSelectedListener = listener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

}