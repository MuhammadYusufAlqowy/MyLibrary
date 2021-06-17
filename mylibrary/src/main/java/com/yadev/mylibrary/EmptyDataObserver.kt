package com.yadev.mylibrary

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyDataObserver(val rclv: RecyclerView, val emptyView: View?) :
    RecyclerView.AdapterDataObserver() {
    init {
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (emptyView != null && rclv.adapter != null) {
            val emptyViewVisible = rclv.adapter!!.itemCount == 0
            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            rclv.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkIfEmpty()
    }
}