package com.yadev.mylibrary

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.cooltechworks.views.shimmer.ShimmerAdapter
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import java.util.*

class MyRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShimmerRecyclerView(context, attrs, defStyleAttr) {
    var emptyView: View? = null
    val emptyObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter != null && emptyView != null) {
                if (adapter.itemCount == 0) {
                    Log.e("ERRR", emptyView.toString())
                    emptyView?.visibility = View.VISIBLE
                    this@MyRecyclerView.visibility = View.GONE
                } else {
                    emptyView?.visibility = View.GONE
                    this@MyRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun showShimmerAdapter() {
        super.showShimmerAdapter()
        if (adapter is ShimmerAdapter) {
            (adapter as ShimmerAdapter).setShimmerColor(Color.WHITE)
            (adapter as ShimmerAdapter).setShimmerAngle(30)
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (actualAdapter != null) {
            if (!adapter?.hasObservers()!!) {
                adapter.registerAdapterDataObserver(emptyObserver)
                emptyObserver.onChanged()
            }
        }
    }


}