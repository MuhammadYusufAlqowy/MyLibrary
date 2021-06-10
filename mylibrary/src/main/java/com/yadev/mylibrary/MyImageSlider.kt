package com.yadev.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.yadev.mylibrary.adapter.BaseMyImageSliderAdapter
import com.yadev.mylibrary.databinding.LayoutMyImageSliderBinding
import java.util.*
import kotlin.concurrent.timerTask


class MyImageSlider(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val layout =
        LayoutMyImageSliderBinding.inflate(LayoutInflater.from(context), this, false)
    var scrollDuration: Long
    var autoScroll: Boolean
    var timer: Timer = Timer()
    var pageMargin: Int
    var pageOffset: Int
    lateinit var update: Runnable
//    var rclv: RecyclerView
    var totalItemCount: Int = 0


    init {
        addView(layout.root)
        layout.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.MyImageSlider)
            autoScroll = attr.getBoolean(R.styleable.MyImageSlider_sliderAutoScroll, true)
            scrollDuration =
                attr.getInt(R.styleable.MyImageSlider_sliderScrollDuration, 3000).toLong()
            val enableIndicator =
                attr.getBoolean(R.styleable.MyImageSlider_sliderEnableIndicator, true)
            val indicatorStyle = attr.getInt(R.styleable.MyImageSlider_sliderIndicatorType, 0)
            val indicatorActiveColor = attr.getColor(
                R.styleable.MyImageSlider_sliderIndicatorActiveColor,
                context.getColorRes(R.color.info)
            )
            val indicatorDisableColor = attr.getColor(
                R.styleable.MyImageSlider_sliderIndicatorDisableColor,
                context.getColorRes(R.color.gray)
            )
            pageMargin = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderMargin,
                context.resources.getDimensionPixelSize(R.dimen._4adp)
            )
            pageOffset = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderOffset,
                context.resources.getDimensionPixelSize(R.dimen._8adp)
            )
            wormIndicator.setDotIndicatorColor(indicatorActiveColor)
            wormIndicator.setStrokeDotsIndicatorColor(indicatorDisableColor)
            dotIndicator.selectedDotColor = indicatorActiveColor
            dotIndicator.dotsColor = indicatorDisableColor
            springIndicator.setDotIndicatorColor(indicatorActiveColor)
            springIndicator.setStrokeDotsIndicatorColor(indicatorDisableColor)

//            rclv = viewPager.getChildAt(0) as RecyclerView

            if (!enableIndicator) {
                wormIndicator.visibility = View.GONE
                dotIndicator.visibility = View.GONE
                springIndicator.visibility = View.GONE
            }

            when (indicatorStyle) {
                0 -> {
                    wormIndicator.visibility = View.GONE
                    springIndicator.visibility = View.GONE
                }
                1 -> {
                    wormIndicator.visibility = View.GONE
                    dotIndicator.visibility = View.GONE
                }
                2 -> {
                    springIndicator.visibility = View.GONE
                    dotIndicator.visibility = View.GONE
                }
            }
            attr.recycle()
        }
    }

    fun <T, V : ViewBinding> setAdapter(adapter: BaseMyImageSliderAdapter<T, V>): BaseMyImageSliderAdapter<T, V> {
        layout.apply {
            viewPager.apply {
                adapter.pageOffset = pageOffset
                adapter.pageMargin = pageMargin
                setAdapter(adapter)
                totalItemCount = adapter.itemCount
                orientation = ORIENTATION_HORIZONTAL
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3

                val pageMarginPx = pageMargin
                val offsetPx = pageOffset
                setPageTransformer { page, position ->
                    val viewPager = page.parent.parent as ViewPager2
                    val offset = position * -(2 * offsetPx + pageMarginPx)
                    if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.translationX = -offset
                        } else {
                            page.translationX = offset
                        }
                    } else {
                        page.translationY = offset
                    }
                }

                /*rclv.apply {
                    addOnScrollListener(
                        InfiniteScrollBehaviour(
                            totalItemCount,
                            layoutManager as LinearLayoutManager
                        )
                    )
                }*/

                var currentPage = currentItem
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        handler.removeCallbacks(update)
                        currentPage = position
                    }

                })
                if (autoScroll) {
                    update = Runnable {
                        viewPager.setCurrentItem(currentPage % adapter.itemCount, true)
                        currentPage++
                    }
                    val timerTask = timerTask {
                        handler.post(update)
                    }
                    timer.schedule(timerTask, scrollDuration, scrollDuration)
                }

            }
            wormIndicator.setViewPager2(viewPager)
            dotIndicator.setViewPager2(viewPager)
            springIndicator.setViewPager2(viewPager)
        }
        return adapter
    }

    inner class InfiniteScrollBehaviour(
        private val itemCount: Int,
        private val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(
            recyclerView: RecyclerView, dx: Int, dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            val lastItemVisible = layoutManager.findLastVisibleItemPosition()
            if (firstItemVisible == (itemCount - 1) && dx > 0) {
                recyclerView.scrollToPosition(1)
            } else if (lastItemVisible == 0 && dx < 0) {
                recyclerView.scrollToPosition(itemCount - 2)
            }
        }
    }

    fun getCurrentItem(): Int {
        return when (layout.viewPager.currentItem) {
            0 -> totalItemCount - 3
            totalItemCount - 1 -> 0
            else -> layout.viewPager.currentItem - 1
        }
    }
}

