package com.yadev.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.yadev.mylibrary.adapter.BaseMyImageSliderAdapter
import com.yadev.mylibrary.databinding.LayoutMyImageSliderBinding
import java.util.*


class MyImageSlider(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val layout =
        LayoutMyImageSliderBinding.inflate(LayoutInflater.from(context), this, false)
    var scrollDuration: Long
    var isInfinte = true
    var isAutoScroll = true
    var pageMargin: Int
    var pageOffset: Int
    var timer: Timer = Timer()
    val update: Runnable? = null
    var totalItemCount: Int = 0

    private lateinit var rclv: RecyclerView

    init {
        addView(layout.root)
        layout.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.MyImageSlider)
            isAutoScroll = attr.getBoolean(R.styleable.MyImageSlider_sliderAutoScroll, true)
            scrollDuration =
                attr.getInt(R.styleable.MyImageSlider_sliderScrollDuration, 3000).toLong()
            val enableIndicator =
                attr.getBoolean(R.styleable.MyImageSlider_sliderEnableIndicator, true)
            val indicatorStyle = attr.getInt(R.styleable.MyImageSlider_sliderIndicatorType, 0)
            val indicatorSize = attr.getDimension(
                R.styleable.MyImageSlider_sliderIndicatorSize,
                resources.getDimension(R.dimen._7adp)
            )
            val indicatorSpacing = attr.getDimension(
                R.styleable.MyImageSlider_sliderIndicatorSpacing,
                resources.getDimension(R.dimen._2adp)
            )
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
            dotIndicator.setDotSize(indicatorSize)
            springIndicator.setDotSize(indicatorSize)
            wormIndicator.setDotSize(indicatorSize)
            dotIndicator.setDotSpacing(indicatorSpacing)
            springIndicator.setDotSpacing(indicatorSpacing)
            wormIndicator.setDotSpacing(indicatorSpacing)

            rclv = viewPager.getChildAt(0) as RecyclerView
            rclv.overScrollMode = OVER_SCROLL_NEVER
            viewPager.updateLayoutParams {
                height = 120.toDp()
            }

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
            viewPager.updateLayoutParams {
                height = resources.getDimensionPixelSize(R.dimen._120adp)
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
//                orientation = ORIENTATION_HORIZONTAL
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                if (adapter.itemCount > 1) {
                    setCurrentItem(1, false)
                    setCurrentItem(0, true)

                    val pageMarginPx = pageMargin
                    val offsetPx = pageOffset
                    Log.wtf("MARGIN: ", "$pageMarginPx \nOFFSET:$pageOffset")
                    setPageTransformer { page, position ->
                        updatePagerHeightForChild(page, this)
                        val viewPager = page.parent.parent as ViewPager2
                        var offset = position * -(2 * offsetPx + pageMarginPx)
                        if (currentItem == 0 && offset <= currentItem + 1 * -(2 * offsetPx + pageMarginPx).toFloat()) offset =
                            position * -(3 * offsetPx + pageMarginPx).toFloat()

                        Log.wtf("POS", position.toString())
                        Log.wtf("OFF", offset.toString())
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

                    /*if (isAutoScroll) {
                        var currentPage = currentItem
                        val update = Runnable {
                            viewPager.setCurrentItem(currentPage % adapter.itemCount, true)
                            currentPage++
                        }
                        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                Handler(Looper.getMainLooper()).removeCallbacks(update)
                                currentPage = position
                            }
                        })

                        val timerTask = timerTask {
                            Handler(Looper.getMainLooper()).post(update)
                        }
                        timer.schedule(timerTask, scrollDuration, scrollDuration)
                    }*/
                    wormIndicator.setViewPager2(viewPager)
                    dotIndicator.setViewPager2(viewPager)
                    springIndicator.setViewPager2(viewPager)
                }
            }

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

    private fun getMeasuredViewHeightFor(view: View): Int {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(wMeasureSpec, hMeasureSpec)
        return view.measuredHeight
    }

    fun recalculate(
        viewPager2: ViewPager2,
        position: Int,
        positionOffset: Float = 0f
    ) =
        ((viewPager2.getChildAt(0) as? RecyclerView)?.layoutManager as? LinearLayoutManager)?.apply {
            val leftView = findViewByPosition(position) ?: return@apply
            val rightView = findViewByPosition(position + 1)
            viewPager2.apply {
                val leftHeight = getMeasuredViewHeightFor(leftView)
                layoutParams = layoutParams.apply {
                    height = if (rightView != null) {
                        val rightHeight = getMeasuredViewHeightFor(rightView)
                        leftHeight + ((rightHeight - leftHeight) * positionOffset).toInt()
                    } else {
                        leftHeight
                    }
                }
                invalidate()
            }
        }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(e)
    }

    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            if (pager.height > view.measuredHeight) {
                pager.layoutParams =
                    (pager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                pager.invalidate()
            }
        }
    }

}

